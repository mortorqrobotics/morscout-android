package org.team1515.morscout.entity;

public class Team extends Entity {

    int number;
    String name;

    public Team(String id, int number, String name) {
        super(id);
        this.number = number;
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }
}
