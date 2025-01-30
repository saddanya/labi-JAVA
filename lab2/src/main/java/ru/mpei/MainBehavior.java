package ru.mpei;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;



public class MainBehavior extends Behaviour {
    public float x;
    public float delta;
    public float result1;
    public float result2;
    public float result3;
    private boolean flag = false;


    MessageTemplate uNumb;
    MessageTemplate keyInitiator;
    MessageTemplate keyFinish;


    public void onStart(){
        uNumb = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        keyInitiator = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        keyFinish = MessageTemplate.MatchPerformative(ACLMessage.CANCEL);
    }

    public void action() {

        ACLMessage msgNumb = myAgent.receive(uNumb);
        ACLMessage msgKey = myAgent.receive(keyInitiator);
        ACLMessage msgFinish = myAgent.receive(keyFinish);

//        Агенты не инициаторы работают здесь
        if (msgNumb != null){
            String msg = msgNumb.getContent();
            String[] split = msg.split(";");
            System.out.println("Я агент " + myAgent.getLocalName() +
                    " получил от агента " + msgNumb.getSender().getLocalName() +
                    " значение X = " + split[0] + " зачение delta = " + split[1]);
            x = Float.parseFloat(split[0]);
            delta = Float.parseFloat(split[1]);

            switch (myAgent.getLocalName()) {
                case "FirstAgent" -> {
                    // Вызов статического метода squaredPolynomial
                    result1 = FunctionCalculation.squaredPolynomial(x - delta);
                    result2 = FunctionCalculation.squaredPolynomial(x);
                    result3 = FunctionCalculation.squaredPolynomial(x + delta);
                }
                case "SecondAgent" -> {
                    // Вызов статического метода Polynomial
                    result1 = FunctionCalculation.Polynomial(x - delta);
                    result2 = FunctionCalculation.Polynomial(x);
                    result3 = FunctionCalculation.Polynomial(x + delta);
                }
                case "ThirdAgent" -> {
                    // Вызов статического метода cos
                    result1 = FunctionCalculation.cos(x - delta);
                    result2 = FunctionCalculation.cos(x);
                    result3 = FunctionCalculation.cos(x + delta);
                }
            }

            System.out.println("Я агент" + myAgent.getLocalName() +
                    "Результаты расчета для X-delta = " + result1 +
                    "\nРезультаты расчета для X = " + result2 +
                    "\nРезультаты расчета для X+delta = " + result3);

            String numb = result1 + ";" + result2 + ";" + result3 + ";" + x + ";" + delta;

//            Отправляем инициатору сообщение с результатами расчета
            ACLMessage msgCalc = new ACLMessage(ACLMessage.PROPOSE);
            AID receiver = new AID(msgNumb.getSender().getLocalName(), false);
            msgCalc.addReceiver(receiver);
            msgCalc.setContent(numb);
            myAgent.send(msgCalc);
            System.out.println("Я агент " + myAgent.getLocalName() + " отправил сообщение " + numb +
                    " агенту " + receiver.getLocalName());
        }
//        Работа инициатора
        if (msgKey != null) {
            System.out.println("Я агент " + myAgent.getLocalName() + " получил ключ инициирования");
            myAgent.addBehaviour(new SendMsgInitiator(myAgent, msgKey.getContent()));
            myAgent.addBehaviour(new ReceiveMsgInitiator());
        }
        if (msgFinish != null) {
            flag = true;
        }
        else {
            block();
        }
    }

    public boolean done() {
        if (flag){
            System.err.println("КОНЕЦ");
        }
        return flag;
    }
}