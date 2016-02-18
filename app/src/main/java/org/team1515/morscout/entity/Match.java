package org.team1515.morscout.entity;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by prozwood on 2/9/16.
 */
public class Match extends Entity {

    String id;
    String number;
    String compLevel;
    JSONArray blueAlliance;
    JSONArray redAlliance;

    public Match(String id, String number, String compLevel, JSONArray blueAlliance, JSONArray redAlliance) {
        super(id);
        this.number = number;
        this.compLevel = compLevel;
        this.blueAlliance = blueAlliance;
        this.redAlliance = redAlliance;
    }

    @Override
    public String getName() {
        return number;
    }

    public String getCompLevel() {
        return compLevel;
    }

    public JSONArray getBlueAlliance() {
        return blueAlliance;
    }

    public JSONArray getRedAlliance() {
        return redAlliance;
    }
}
