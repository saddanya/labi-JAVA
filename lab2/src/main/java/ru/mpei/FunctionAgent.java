package ru.mpei;

import jade.core.Agent;

public class FunctionAgent extends Agent {

    protected void setup() {
        // В поведении задаем начальные значения и назначаем первого инициатором
        if (this.getLocalName().equals("FirstAgent")) {
            addBehaviour(new FirstSend(this.getLocalName()));
        }
        // Агент добавляет поведение для принятия запросов и отправки результатов
        addBehaviour(new MainBehavior());
    }
}