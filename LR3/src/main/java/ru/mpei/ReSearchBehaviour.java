package ru.mpei;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ru.mpei.dop.AgentCfg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReSearchBehaviour extends Behaviour {
    AgentCfg cfg;
    List<String> AgentsList = new ArrayList<>();
    MessageTemplate key;

    public ReSearchBehaviour(AgentCfg cfg) {
        this.cfg = cfg;
    }
    @Override
    public void onStart(){
        key = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
    }
    @Override
    public void action() {
        ACLMessage msgInSearch = myAgent.receive(key);

        if (msgInSearch != null) {
            //System.out.println(myAgent.getLocalName() + " получил сообщение"+ " от " + msgInSearch.getSender().getLocalName() +": "+ msgInSearch.getContent());

            // Парсим содержимое сообщения
            String[] content = msgInSearch.getContent().split(",");
            List<String> path = new ArrayList<>(Arrays.asList(content)); // Список узлов в пути и расстояния

            // Агент искомый формирует сообщение для обратной передачи
            ACLMessage reverseMsg = new ACLMessage(ACLMessage.REQUEST);
            reverseMsg.setContent(msgInSearch.getContent() +","+ myAgent.getLocalName());
            reverseMsg.addReceiver(new AID(path.get(path.size() - 2), false)); // Отправляем предшественнику
            //System.out.println(myAgent.getLocalName() + " отправляет " + path.get(path.size()-2) + " обратное сообщение: " + reverseMsg.getContent());
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