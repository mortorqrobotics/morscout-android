package org.team1515.morscout.entities;

/**
 * Created by prozwood on 2/9/16.
 */
public class Team extends Entity {

    String number;

    public Team(String number) {
        super(number);
        this.number = number;
    }

    @Override
    public String getName() {
        return number;
    }
}
