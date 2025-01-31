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

    @Override
    public void onStart() {
        uNumb = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        keyInitiator = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        keyFinish = MessageTemplate.MatchPerformative(ACLMessage.CANCEL);
    }

    @Override
    public void action() {
        ACLMessage msgNumb = myAgent.receive(uNumb);
        ACLMessage msgKey = myAgent.receive(keyInitiator);
        ACLMessage msgFinish = myAgent.receive(keyFinish);

        if (msgNumb != null) {
            processDataMessage(msgNumb);
        }

        if (msgKey != null) {
            processInitiationMessage(msgKey);
        }

        if (msgFinish != null) {
            processFinishMessage();
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        if (flag) {
            System.err.println("КОНЕЦ");
        }
        return flag;
    }

    // Обработка сообщения с данными
    private void processDataMessage(ACLMessage msgNumb) {
        String msg = msgNumb.getContent();
        String[] split = msg.split(";");
        System.out.println("Я агент " + myAgent.getLocalName() +
                " получил от агента " + msgNumb.getSender().getLocalName() +
                " значение X = " + split[0] + " значение delta = " + split[1]);

        x = Float.parseFloat(split[0]);
        delta = Float.parseFloat(split[1]);

        calculateResults();
        sendResultsToInitiator(msgNumb.getSender());
    }

    // Вычисление результатов в зависимости от типа агента
    private void calculateResults() {
        switch (myAgent.getLocalName()) {
            case "FirstAgent" -> {
                result1 = FunctionCalculation.squaredPolynomial(x - delta);
                result2 = FunctionCalculation.squaredPolynomial(x);
                result3 = FunctionCalculation.squaredPolynomial(x + delta);
            }
            case "SecondAgent" -> {
                result1 = FunctionCalculation.Polynomial(x - delta);
                result2 = FunctionCalculation.Polynomial(x);
                result3 = FunctionCalculation.Polynomial(x + delta);
            }
            case "ThirdAgent" -> {
                result1 = FunctionCalculation.cos(x - delta);
                result2 = FunctionCalculation.cos(x);
                result3 = FunctionCalculation.cos(x + delta);
            }
        }

        System.out.println("Я агент " + myAgent.getLocalName() +
                "\nРезультаты расчета для X-delta = " + result1 +
                "\nРезультаты расчета для X = " + result2 +
                "\nРезультаты расчета для X+delta = " + result3);
    }

    // Отправка результатов инициатору
    private void sendResultsToInitiator(AID initiatorAID) {
        String numb = result1 + ";" + result2 + ";" + result3 + ";" + x + ";" + delta;
        ACLMessage msgCalc = new ACLMessage(ACLMessage.PROPOSE);
        msgCalc.addReceiver(initiatorAID);
        msgCalc.setContent(numb);
        myAgent.send(msgCalc);

        System.out.println("Я агент " + myAgent.getLocalName() + " отправил сообщение " + numb +
                " агенту " + initiatorAID.getLocalName());
    }

    // Обработка сообщения с ключом инициирования
    private void processInitiationMessage(ACLMessage msgKey) {
        System.out.println("Я агент " + myAgent.getLocalName() + " получил ключ инициирования");
        myAgent.addBehaviour(new SendMsgInitiator(myAgent, msgKey.getContent()));
        myAgent.addBehaviour(new ReceiveMsgInitiator());
    }

    // Обработка сообщения с ключом завершения
    private void processFinishMessage() {
        flag = true;
    }
}