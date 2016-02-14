package org.team1515.morscout.entity;

public class Team extends Entity {

    String number;
    String name;

    public Team(String id, String number, String name) {
        super(id);
        this.number = number;
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }
}
