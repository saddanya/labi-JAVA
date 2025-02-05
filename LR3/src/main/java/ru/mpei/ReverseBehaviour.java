package ru.mpei;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ru.mpei.dop.AgentCfg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReverseBehaviour extends Behaviour {
    AgentCfg cfg;
    MessageTemplate keyRevers;

    public ReverseBehaviour(AgentCfg cfg) {
        this.cfg = cfg;
    }

    @Override
    public void onStart(){
        keyRevers = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
    }

    @Override
    public void action() {

        ACLMessage msgIn = myAgent.receive(keyRevers);
        if (msgIn != null) {
            //System.out.println(myAgent.getLocalName() + " получил обратное сообщение от "+ msgIn.getSender().getLocalName() +" содержанием: "+ msgIn.getContent());

            // Парсим содержимое сообщения
            String[] content = msgIn.getContent().split(",");
            List<String> path = new ArrayList<>(Arrays.asList(content)); // Список узлов в пути и расстояния

            // Передаем сообщение следующему агенту в обратном направлении
            String previousAgent = path.get(path.indexOf(msgIn.getSender().getLocalName()) - 2); // Предыдущий агент в пути
            ACLMessage reverseMsg = new ACLMessage(ACLMessage.REQUEST);
            reverseMsg.setContent(msgIn.getContent());
            reverseMsg.addReceiver(new AID(previousAgent, false));
            //System.out.println(myAgent.getLocalName() + " отправляет обратное сообщение " + previousAgent + " содержанием: " + reverseMsg.getContent());
            myAgent.send(reverseMsg);

        } else {
            block(); // Блокируем поведение до получения нового сообщения
        }
    }

    @Override
    public boolean done() {
        return false;
    }

//    // Метод для вычисления общей длины пути
//    private int calculateTotalWeight(List<String> path) {
//        int totalWeight = 0;
//        for (int i = 1; i < path.size(); i += 2) {
//            totalWeight += Integer.parseInt(path.get(i));
//        }
//        return totalWeight;
//    }
}