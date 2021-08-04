package com.example.football.models.dto.xmlDto;

import com.example.football.models.entity.enums.PlayerPosition;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerSeedDto {

    @XmlElement(name = "first-name")
    private String firstName;
    @XmlElement(name = "last-name")
    private String lastName;
    @XmlElement(name = "email")
    private String email;
    @XmlElement(name = "birth-date")
    private String birthDate;
    @XmlElement(name = "position")
    private PlayerPosition position;
    @XmlElement(name = "town")
    private TownNameDto townName;
    @XmlElement(name = "team")
    private TeamNameDto teamName;
    @XmlElement(name = "stat")
    private StatIdDto statId;


    @Size(min = 2)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Size(min = 2)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }


    public PlayerPosition getPosition() {
        return position;
    }

    public void setPosition(PlayerPosition position) {
        this.position = position;
    }


    public TownNameDto getTownName() {
        return townName;
    }


    public void setTownName(TownNameDto townName) {
        this.townName = townName;
    }


    public TeamNameDto getTeamName() {
        return teamName;
    }

    public void setTeamName(TeamNameDto teamName) {
        this.teamName = teamName;
    }


    public StatIdDto getStatId() {
        return statId;
    }

    public void setStatId(StatIdDto statId) {
        this.statId = statId;
    }
}
