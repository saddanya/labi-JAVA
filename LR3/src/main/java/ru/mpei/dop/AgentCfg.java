package ru.mpei.dop;


import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name ="cfg")
@XmlAccessorType(XmlAccessType.FIELD)
public class AgentCfg {
    @XmlElement()
    private String name;
    @XmlElement
    private String initiator;
    @XmlElement
    private String search;
    @XmlElement
    private NearInf nearInf;
}
