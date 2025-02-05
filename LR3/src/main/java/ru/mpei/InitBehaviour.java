package ru.mpei;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import ru.mpei.dop.AgentCfg;

public class InitBehaviour extends OneShotBehaviour {
    AgentCfg cfg;

    public InitBehaviour(AgentCfg agentCfg) {
        this.cfg = agentCfg;
    }

    @Override
    public void action() {
        for (int i =0; i < cfg.getNearInf().getNear().size(); i++){
            ACLMessage msgOut = new ACLMessage(ACLMessage.INFORM);
            msgOut.setContent(myAgent.getLocalName() +"," + cfg.getNearInf().getWeight().get(i));
            msgOut.addReceiver(new AID(cfg.getNearInf().getNear().get(i), false));
            System.out.println("Сообщение от " + myAgent.getLocalName() +
                    " агенту " + cfg.getNearInf().getNear().get(i) +" содержанием: " + msgOut.getContent());
            myAgent.send(msgOut);
        }
    }
}