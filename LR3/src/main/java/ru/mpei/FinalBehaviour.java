package ru.mpei;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ru.mpei.dop.AgentCfg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FinalBehaviour extends Behaviour {
    AgentCfg cfg;
    MessageTemplate keyRevers;

    // Переменные для хранения кратчайшего пути и его длины
    List<String> shortestPath = new ArrayList<>();
    List<String> filePath = new ArrayList<>();
    int shortestPathWeight = Integer.MAX_VALUE;
    private long lastMessageTime = System.currentTimeMillis(); // Время получения последнего сообщения
    private static final long TIMEOUT = 500; // Таймаут ожидания в миллисекундах

    public FinalBehaviour(AgentCfg cfg) {
        this.cfg = cfg;
    }

    @Override
    public void onStart() {
        keyRevers = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
    }

    @Override
    public void action() {
        ACLMessage msgIn = myAgent.receive(keyRevers);
        if (msgIn != null) {
            // Обновляем время последнего сообщения
            lastMessageTime = System.currentTimeMillis();
            //System.out.println(myAgent.getLocalName() + " получил обратное сообщение от " + msgIn.getSender().getLocalName() + " содержанием: " + msgIn.getContent());

            // Парсим содержимое сообщения
            String[] content = msgIn.getContent().split(",");
            List<String> path = new ArrayList<>(Arrays.asList(content)); // Список узлов в пути и расстояния
            int pathWeight = 0;
            // Вычисление общей длины пути
            if (!Objects.equals(path.get(path.size() - 1), "fail")) {
                for (int i = 1; i < path.size(); i += 2) {
                    pathWeight += Integer.parseInt(path.get(i));
                }
                // Сравниваем текущий вес с минимальным
                if (pathWeight < shortestPathWeight) {
                    shortestPathWeight = pathWeight;  // Обновляем минимальный вес
                    shortestPath = new ArrayList<>(path); // Обновляем путь
                }
            } else {
                filePath = new ArrayList<>(path); // Обновляем путь
                //System.err.println("Тупик: " + String.join(" -> ", filePath));
            }

        } else {
            block(100); // Блокируем поведение на 1с
        }
    }

    @Override
    public boolean done() {
        // Проверяем, истек ли таймаут
        if (System.currentTimeMillis() - lastMessageTime > TIMEOUT) {
            // Выводим самый короткий путь перед завершением
            System.err.println("Самый короткий путь: " + String.join(" -> ", shortestPath));
            System.err.println("Вес самого короткого пути: " + shortestPathWeight);

            return true; // Завершаем поведение
        }
        return false; // Продолжаем ожидание сообщений
    }
}
//Самый короткий путь: Agent1 -> 9 -> Agent4 -> 3 -> Agent7 -> 16 -> Agent9 -> 7 -> Agent10 -> 5 -> Agent12
//Вес самого короткого пути: 40