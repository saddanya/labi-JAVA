package AgentDetector;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

// Класс для отправки UDP-сообщений
public class UdpPublisher {
    private DatagramSocket socket; // Сокет для отправки UDP-пакетов
    private int targetPort; // Порт назначения
    private InetAddress targetIp; // IP-адрес назначения

    // Метод для инициализации UDP-публикации
    public void initialize(String ip, int port) {
        this.targetPort = port; // Устанавливаем порт назначения
        try {
            this.targetIp = InetAddress.getByName(ip); // Устанавливаем IP-адрес назначения
        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve IP address", e);
        }

        try {
            socket = new DatagramSocket(); // Создаем UDP-сокет
        } catch (SocketException e) {
            throw new RuntimeException("Failed to create socket", e);
        }
    }

    // Метод для отправки сообщения
    public void broadcast(String message) {
        DatagramPacket packet = new DatagramPacket(
                message.getBytes(),
                message.length(),
                targetIp,
                targetPort
        ); // Создаем UDP-пакет
        try {
            socket.send(packet); // Отправляем пакет
        } catch (IOException e) {
            throw new RuntimeException("Failed to send UDP packet", e);
        }
    }
}