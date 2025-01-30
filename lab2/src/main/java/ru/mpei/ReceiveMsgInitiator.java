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

    float numb1;
    float numb2;
    float numb3;
    float x;
    float delta;




    public void action() {
        mResult = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);

        ACLMessage msgResult = myAgent.receive(mResult);
        if (msgResult != null) {
            // Получаем данные от агента
            String msg = msgResult.getContent();
            String[] data = msg.split(";");
            numb1 = Float.parseFloat(data[0]);
            numb2 = Float.parseFloat(data[1]);
            numb3 = Float.parseFloat(data[2]);
            x = Float.parseFloat(data[3]);
            delta = Float.parseFloat(data[4]);

            // Увеличиваем счетчик полученных сообщений
            count++;
            sum1 += numb1;
            sum2 += numb2;
            sum3 += numb3;

            // Произвести расчет, когда все агенты отправили данные
            if (count == 3) {
                System.out.println("Сумма = "+ sum1);
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

                // Проверка завершения по точности
                if (nextDelta < 0.01) {
                    flag = true;
                    System.out.println("Точность достигнута. Завершаем расчет.");
                    ACLMessage finall = new ACLMessage(ACLMessage.CANCEL);
                    finall.setContent(String.valueOf(sum2));
                    finall.addReceiver(new AID(myAgent.getLocalName(), false));
                    myAgent.send(finall);

                } else {
                    // Передача роли инициатора случайному агенту

                    String nextInitiator = "";

                    // Рандомно определяем следующего инициатора
                    if (myAgent.getLocalName().equals("FirstAgent")) {
                        if (Math.random() < 0.5) {
                            nextInitiator = "SecondAgent";
                        } else {
                            nextInitiator = "ThirdAgent";
                        }
                    } else if (myAgent.getLocalName().equals("SecondAgent")) {
                        if (Math.random() < 0.5) {
                            nextInitiator = "FirstAgent";
                        } else {
                            nextInitiator = "ThirdAgent";
                        }
                    } else if (myAgent.getLocalName().equals("ThirdAgent")) {
                        if (Math.random() < 0.5) {
                            nextInitiator = "FirstAgent";
                        } else {
                            nextInitiator = "SecondAgent";
                        }
                    }
                    if (nextInitiator.isEmpty()) {
                        System.err.println("Ошибка: не удалось определить следующего инициатора!");
                    }
                    ACLMessage key = new ACLMessage(ACLMessage.REQUEST);
                    key.addReceiver(new AID(nextInitiator, false));
                    key.setContent(nextX + ";" + nextDelta);
                    myAgent.send(key);
                    System.out.println("Передаем роль инициатора агенту " + nextInitiator);

                    // Завершение выполнения поведения после отправки сообщений, т.к. может агент снова стать
                    // инициатором и запустить второе такое же поведение - скрытая ошибка.
                    myAgent.removeBehaviour(this);
                }
            }
        } else {
            block();
        }
    }


    public boolean done() {
        return flag;
    }
}