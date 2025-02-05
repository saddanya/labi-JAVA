package ru.mpei;

import jade.core.Agent;
import ru.mpei.dop.AgentCfg;
import ru.mpei.dop.XmlSerialization;

public class NodeAgent extends Agent {

    @Override
    protected void setup() {

        Object[] args = this.getArguments();
        AgentCfg agentCfg = XmlSerialization.deserialize(AgentCfg.class, args[0].toString());
        System.out.println(this.getLocalName() + " starts work");

        if (agentCfg.getInitiator().equals("True")) {
            addBehaviour(new InitBehaviour(agentCfg));
            addBehaviour(new FinalBehaviour(agentCfg));
        }
        if (agentCfg.getInitiator().equals("False") & agentCfg.getSearch().equals("False")){
            addBehaviour(new RequestBehaviour(agentCfg));
            addBehaviour(new ReverseBehaviour (agentCfg));
        }
        if (agentCfg.getSearch().equals("True")){
            addBehaviour(new ReSearchBehaviour(agentCfg));
        }

    }
}



