package AgentRuntime;

import AgentDetector.AgentDetector;
import AgentDetector.Detector;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

// Класс агента, который публикует свою информацию и обнаруживает других агентов
public class CustomAgent extends Agent {
    private AID agentId; // Идентификатор агента

    @Override
    protected void setup() {
        this.agentId = new AID(getLocalName(), true); // Создаем идентификатор агента
        System.out.println("Agent started: " + getLocalName()); // Выводим сообщение о старте агента

        // Добавляем таймер для завершения работы агента через 5 секунд
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Agent " + getLocalName() + " is terminating after 5 seconds.");
                doDelete(); // Завершаем работу агента
            }
        }, 500000); // 5000 миллисекунд = 5 секунд

        // Публикация информации об агенте
        addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                AgentDetector detector = new Detector();
                detector.startPublishing(agentId, 9000); // Публикуем информацию об агенте на порту 9000
                System.out.println(getLocalName() + " published its info.");
            }
        });

        // Обнаружение других агентов
        addBehaviour(new TickerBehaviour(this, 5000) {
            @Override
            protected void onTick() {
                AgentDetector detector = new Detector();
                detector.startDiscovering(9000); // Начинаем обнаружение других агентов на порту 9000
                List<AID> activeAgents = detector.getActiveAgents(); // Получаем список активных агентов
                System.out.println(getLocalName() + " found agents: " + activeAgents);
            }
        });
    }

    @Override
    protected void takeDown() {
        System.out.println("Agent " + getLocalName() + " is terminating."); // Выводим сообщение о завершении работы агента
        super.takeDown();
    }
}