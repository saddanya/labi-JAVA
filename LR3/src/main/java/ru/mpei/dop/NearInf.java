package ru.mpei.dop;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class NearInf {

    @XmlElementWrapper(name = "nears")
    @XmlElement(name = "near")
    private List<String> near;

    @XmlElementWrapper(name = "weights")
    @XmlElement(name = "weight")
    private List<String> weight;
}