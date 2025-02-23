package ru.mpei;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ru.mpei.dop.AgentCfg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestBehaviour extends Behaviour {
    MessageTemplate key;
    AgentCfg cfg;
    List<String> AgentsList = new ArrayList<>();
    int current = 0;

    public RequestBehaviour(AgentCfg cfg) {
        this.cfg = cfg;
    }

    @Override
    public void onStart(){
        key = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
    }
    @Override
    public void action() {
        ACLMessage msgIn = myAgent.receive(key);
        if (msgIn != null){
            System.out.println(myAgent.getLocalName() + " получил сообщение от "+ msgIn.getSender().getLocalName() +" содержанием: "+ msgIn.getContent());
            String[] content = msgIn.getContent().split(",");

            // Создаем массив агентов в пути
            for (int i = 0; i< content.length; i += 2){
                AgentsList.add(content[i]);
            }
//            System.out.println(AgentsList);

                // отправляем сообщения ближайшим агентам
            for (int i =0; i < cfg.getNearInf().getNear().size(); i++){
                // проверяем на то, чтобы сообщение НЕ отправилось одному из предыдущих агентов
                if(!AgentsList.contains(cfg.getNearInf().getNear().get(i))){
                    ACLMessage msgOut = new ACLMessage(ACLMessage.INFORM);
                    // добавляем к предыдущему массиву отправителя и расстояние до получателя
                    msgOut.setContent(msgIn.getContent() +","+ myAgent.getLocalName() +"," + cfg.getNearInf().getWeight().get(i));
                    msgOut.addReceiver(new AID(cfg.getNearInf().getNear().get(i), false));
                    //System.out.println("Сообщение от " + myAgent.getLocalName() + " агенту " + cfg.getNearInf().getNear().get(i) +" содержанием: " + msgOut.getContent());
                    myAgent.send(msgOut);
                    current++;
                }
            }
            if (current == 0){
                String[] cont = msgIn.getContent().split(",");
                List<String> path = new ArrayList<>(Arrays.asList(cont)); // Список узлов в пути и расстояния

                // Передаем сообщение следующему агенту в обратном направлении
                String previousAgent = path.get(path.size() - 2); // Предыдущий агент в пути
                ACLMessage reverseMsg = new ACLMessage(ACLMessage.REQUEST);
                reverseMsg.setContent(msgIn.getContent() + "," + myAgent.getLocalName() +",fail");
                reverseMsg.addReceiver(new AID(previousAgent, false));
                //System.out.println(myAgent.getLocalName() + " отправляет обратное сообщение fail " + previousAgent + " содержанием: " + reverseMsg.getContent());
                myAgent.send(reverseMsg);
            }
            AgentsList.clear();
            current = 0;
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}