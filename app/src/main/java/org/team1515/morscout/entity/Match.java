package org.team1515.morscout.entity;

import org.json.JSONArray;

/**
 * Created by prozwood on 2/9/16.
 */
public class Match extends Entity {

    String id;
    String number;

    public Match(String id, String number) {
        super(id);
        this.number = number;
    }

    @Override
    public String getName() {
        return number;
    }
}
