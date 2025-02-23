package AgentDetector;

import jade.core.AID;
import java.util.List;

// Интерфейс, определяющий методы для работы с агентами
public interface AgentDetector {
    // Метод для начала публикации информации об агенте на указанном порту
    void startPublishing(AID agentId, int port);

    // Метод для начала обнаружения других агентов на указанном порту
    void startDiscovering(int port);

    // Метод для получения списка активных агентов
    List<AID> getActiveAgents();
}