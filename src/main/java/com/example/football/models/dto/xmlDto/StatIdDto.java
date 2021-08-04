package com.example.football.models.dto.xmlDto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigInteger;

@XmlAccessorType(XmlAccessType.FIELD)
public class StatIdDto {

    @XmlElement(name = "id")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
