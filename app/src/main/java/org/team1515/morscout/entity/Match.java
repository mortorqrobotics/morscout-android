package org.team1515.morscout.entity;

/**
 * Created by prozwood on 2/9/16.
 */
public class Match extends Entity {

    String id;

    public Match(String id) {
        super(id);
        this.id = id;
    }

    @Override
    public String getName() {
        return id;
    }
}
