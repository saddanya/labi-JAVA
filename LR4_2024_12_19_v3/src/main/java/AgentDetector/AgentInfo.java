package AgentDetector;

import jade.core.AID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Класс, представляющий информацию об агенте
@Data // Аннотация Lombok для генерации геттеров, сеттеров, toString и т.д.
@NoArgsConstructor // Аннотация Lombok для генерации конструктора без параметров
@AllArgsConstructor // Аннотация Lombok для генерации конструктора со всеми параметрами
public class AgentInfo {
    private AID agentId; // Идентификатор агента
    private boolean isGuid; // Флаг, указывающий, является ли идентификатор глобальным (GUID)
    private long lastUpdateTime; // Время последнего обновления информации об агенте

    // Конструктор для создания объекта AgentInfo
    public AgentInfo(AID agentId, boolean isGuid) {
        this.agentId = agentId;
        this.isGuid = isGuid;
        this.lastUpdateTime = System.currentTimeMillis(); // Устанавливаем текущее время
    }
}