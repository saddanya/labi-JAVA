package AgentDetector;

import jade.core.AID;
import java.util.List;

// Класс, реализующий интерфейс AgentDetector
public class Detector implements AgentDetector {
    private final UdpPublisher publisher = new UdpPublisher(); // Объект для отправки UDP-сообщений
    private final UdpListener listener = new UdpListener(); // Объект для приема UDP-сообщений

    // Метод для начала публикации информации об агенте
    @Override
    public void startPublishing(AID agentId, int port) {
        publisher.initialize("127.0.0.1", port); // Инициализация UDP-публикации на локальном IP и указанном порту
        AgentInfo agentInfo = new AgentInfo(agentId, true); // Создание объекта AgentInfo
        JsonSerializer.toJson(agentInfo).ifPresent(publisher::broadcast); // Сериализация и отправка информации об агенте
    }

    // Метод для начала обнаружения других агентов
    @Override
    public void startDiscovering(int port) {
        listener.startListening(port); // Запуск прослушивания UDP-сообщений на указанном порту
    }

    // Метод для получения списка активных агентов
    @Override
    public List<AID> getActiveAgents() {
        return listener.getDetectedAgents(); // Возвращаем список обнаруженных агентов
    }
}