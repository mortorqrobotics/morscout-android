package org.team1515.morscout.entity;

/**
 * Created by prozwood on 2/9/16.
 */
public class Match extends Entity {

    String id;
    String number;
    String time;

    public Match(String id, String number, String time) {
        super(id);
        this.number = number;
        this.time = time;
    }

    @Override
    public String getName() {
        return number;
    }

    public String getTime() {
        return time;
    }
}
