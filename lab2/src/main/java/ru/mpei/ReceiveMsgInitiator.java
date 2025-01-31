package ru.mpei;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReceiveMsgInitiator extends Behaviour {
    private MessageTemplate mResult;
    private int count = 0;
    private boolean flag = false;
    public float nextX;
    public float nextDelta;
    private float sum1 = 0;
    private float sum2 = 0;
    private float sum3 = 0;
    private float numb1;
    private float numb2;
    private float numb3;
    private float x;
    private float delta;

    @Override
    public void action() {
        mResult = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
        ACLMessage msgResult = myAgent.receive(mResult);

        if (msgResult != null) {
            processResultMessage(msgResult);
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return flag;
    }

    // Обработка сообщения с результатами
    private void processResultMessage(ACLMessage msgResult) {
        extractDataFromMessage(msgResult);
        count++;
        updateSums();

        if (count == 3) {
            calculateNextStep();
            if (checkCompletion()) {
                finishCalculation();
            } else {
                transferInitiatorRole();
                myAgent.removeBehaviour(this);
            }
        }
    }

    // Извлечение данных из сообщения
    private void extractDataFromMessage(ACLMessage msgResult) {
        String msg = msgResult.getContent();
        String[] data = msg.split(";");
        numb1 = Float.parseFloat(data[0]);
        numb2 = Float.parseFloat(data[1]);
        numb3 = Float.parseFloat(data[2]);
        x = Float.parseFloat(data[3]);
        delta = Float.parseFloat(data[4]);
    }

    // Обновление сумм
    private void updateSums() {
        sum1 += numb1;
        sum2 += numb2;
        sum3 += numb3;
    }

    // Вычисление следующего шага
    private void calculateNextStep() {
        System.out.println("Сумма = " + sum1);
        if (sum1 > sum2 && sum1 > sum3) {
            nextX = x - delta;
            nextDelta = delta;
        } else if (sum3 > sum1 && sum3 > sum2) {
            nextX = x + delta;
            nextDelta = delta;
        } else {
            nextX = x;
            nextDelta = delta / 2;
        }
    }

    // Проверка завершения по точности
    private boolean checkCompletion() {
        return nextDelta < 0.01;
    }

    // Завершение расчета
    private void finishCalculation() {
        flag = true;
        System.out.println("Точность достигнута. Завершаем расчет.");
        ACLMessage finall = new ACLMessage(ACLMessage.CANCEL);
        finall.setContent(String.valueOf(sum2));
        finall.addReceiver(new AID(myAgent.getLocalName(), false));
        myAgent.send(finall);
    }

    // Передача роли инициатора
    private void transferInitiatorRole() {
        String nextInitiator = determineNextInitiator();
        if (nextInitiator.isEmpty()) {
            System.err.println("Ошибка: не удалось определить следующего инициатора!");
            return;
        }

        ACLMessage key = new ACLMessage(ACLMessage.REQUEST);
        key.addReceiver(new AID(nextInitiator, false));
        key.setContent(nextX + ";" + nextDelta);
        myAgent.send(key);
        System.out.println("Передаем роль инициатора агенту " + nextInitiator);
    }

    // Определение следующего инициатора
    private String determineNextInitiator() {
        String nextInitiator = "";
        String currentAgent = myAgent.getLocalName();

        if (currentAgent.equals("FirstAgent")) {
            nextInitiator = (Math.random() < 0.5) ? "SecondAgent" : "ThirdAgent";
        } else if (currentAgent.equals("SecondAgent")) {
            nextInitiator = (Math.random() < 0.5) ? "FirstAgent" : "ThirdAgent";
        } else if (currentAgent.equals("ThirdAgent")) {
            nextInitiator = (Math.random() < 0.5) ? "FirstAgent" : "SecondAgent";
        }

        return nextInitiator;
    }
}