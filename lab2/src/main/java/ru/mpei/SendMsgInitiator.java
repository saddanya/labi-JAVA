package ru.mpei;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class SendMsgInitiator extends OneShotBehaviour {
    private Agent myAgent;
    private String data;

    public SendMsgInitiator(Agent myAgent, String data){
        this.myAgent = myAgent;
        this.data = data;
    }
    public void action(){
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        AID receiver1 = new AID("FirstAgent",false);
        AID receiver2 = new AID("SecondAgent",false);
        AID receiver3 = new AID("ThirdAgent",false);
        msg.addReceiver(receiver1);
        msg.addReceiver(receiver2);
        msg.addReceiver(receiver3);
        msg.setContent(data);
        myAgent.send(msg);
        System.out.println("Я агент-инициатор " + myAgent.getLocalName() +
                " отправил всем агентам значения для построения " + data);
    }
}
