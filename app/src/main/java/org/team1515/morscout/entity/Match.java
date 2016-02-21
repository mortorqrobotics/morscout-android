package org.team1515.morscout.entity;

import org.json.JSONArray;

import java.util.List;

public class Match extends Entity {

    String number;
    String compLevel;
    String[] blueAlliance;
    String[] redAlliance;

    public Match(String id, String number, String compLevel, String[] blueAlliance, String[] redAlliance) {
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

    public String[] getBlueAlliance() {
        return blueAlliance;
    }

    public String[] getRedAlliance() {
        return redAlliance;
    }
}
