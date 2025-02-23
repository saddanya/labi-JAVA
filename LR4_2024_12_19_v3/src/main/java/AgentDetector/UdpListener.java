package AgentDetector;

import com.sun.jna.NativeLibrary;
import jade.core.AID;
import lombok.Getter;
import lombok.SneakyThrows;
import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;

import java.util.*;

// Класс для приема UDP-сообщений и обработки информации об агентах
public class UdpListener {
    static {
        NativeLibrary.addSearchPath("wpcap", "C:\\Windows\\System32\\Npcap"); // Устанавливаем путь к библиотеке Npcap
    }

    @Getter
    private final Map<AID, AgentInfo> detectedAgents = new HashMap<>(); // Карта для хранения обнаруженных агентов
    private PcapHandle pcapHandle; // Объект для работы с сетевыми пакетами

    // Метод для начала прослушивания UDP-сообщений
    @SneakyThrows
    public void startListening(int port) {
        List<PcapNetworkInterface> devices = Pcaps.findAllDevs(); // Получаем список сетевых интерфейсов
        PcapNetworkInterface loopback = devices.stream()
                .filter(dev -> dev.getName().equals("\\Device\\NPF_Loopback")) // Ищем петлевой интерфейс
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Loopback device not found")); // Выбрасываем исключение, если интерфейс не найден

        pcapHandle = loopback.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10); // Открываем интерфейс для захвата пакетов

        Thread listenerThread = new Thread(() -> {
            try {
                pcapHandle.setFilter("udp and dst port " + port, BpfProgram.BpfCompileMode.OPTIMIZE); // Устанавливаем фильтр для UDP-пакетов на указанный порт
                pcapHandle.loop(-1, this::processPacket); // Запускаем бесконечный цикл для обработки пакетов
            } catch (Exception e) {
                throw new RuntimeException("Error in packet processing", e);
            }
        });
        listenerThread.start(); // Запускаем поток для прослушивания пакетов

        // Запускаем отдельный поток для проверки актуальности агентов
        Thread cleanupThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000); // Проверяем каждые 5 секунд
                    cleanupStaleAgents(); // Удаляем устаревших агентов
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        cleanupThread.setDaemon(true); // Устанавливаем поток как демон
        cleanupThread.start(); // Запускаем поток
    }

    // Метод для обработки полученного пакета
    private void processPacket(Packet packet) {
        byte[] rawData = packet.getRawData(); // Получаем необработанные данные пакета
        String payload = new String(rawData, 32, rawData.length - 32); // Извлекаем полезную нагрузку
        Optional<AgentInfo> agentInfo = JsonSerializer.fromJson(payload, AgentInfo.class); // Десериализуем JSON в объект AgentInfo
        agentInfo.ifPresent(info -> {
            info.setLastUpdateTime(System.currentTimeMillis()); // Обновляем время последнего обновления
            detectedAgents.put(info.getAgentId(), info); // Добавляем или обновляем информацию об агенте
        });
    }

    // Метод для удаления устаревших агентов
    private void cleanupStaleAgents() {
        long currentTime = System.currentTimeMillis(); // Текущее время
        detectedAgents.entrySet().removeIf(entry -> {
            long lastUpdateTime = entry.getValue().getLastUpdateTime(); // Время последнего обновления агента
            return (currentTime - lastUpdateTime) > 10000; // Удаляем, если агент не обновлялся более 10 секунд
        });
    }

    // Метод для получения списка активных агентов
    public List<AID> getDetectedAgents() {
        return new ArrayList<>(detectedAgents.keySet()); // Возвращаем список идентификаторов активных агентов
    }
}