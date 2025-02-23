package AgentDetector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;

// Класс для сериализации и десериализации объектов в JSON и обратно
public class JsonSerializer {
    private static final ObjectMapper mapper; // Объект для работы с JSON

    static {
        mapper = new ObjectMapper(); // Инициализация ObjectMapper
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Настройка: игнорировать неизвестные поля
    }

    // Метод для сериализации объекта в JSON
    public static Optional<String> toJson(Object obj) {
        try {
            return Optional.of(mapper.writeValueAsString(obj)); // Сериализация объекта в строку JSON
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Optional.empty(); // Возвращаем пустой Optional в случае ошибки
        }
    }

    // Метод для десериализации JSON в объект
    public static <T> Optional<T> fromJson(String json, Class<T> type) {
        try {
            return Optional.of(mapper.readValue(json, type)); // Десериализация строки JSON в объект
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty(); // Возвращаем пустой Optional в случае ошибки
        }
    }
}