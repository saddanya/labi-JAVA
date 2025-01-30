package ru.mpei;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class FirstSend extends OneShotBehaviour {

//    Старт программы. Передаем первому агенту ключ инициализации, он главный пока что...(((
    private String name;

    public FirstSend(String name) {
        this.name = name;
    }

    public void action() {

        float x1 = (float) Math.random()*1000;
        float delta = 10;
        System.out.println("Я агент " + name + " стартовые значения X=" + x1 + " delta = " + delta);

        String d = String.valueOf(delta);
        String x = String.valueOf(x1);
        String key = x + ";" + d;

        ACLMessage firstMsg = new ACLMessage(ACLMessage.REQUEST);
        AID receiver = new AID(name,false);
        firstMsg.addReceiver(receiver);
        firstMsg.setContent(key);
        myAgent.send(firstMsg);
    }
}