package org.team1515.morscout.entity;

public class Match extends Entity {

    int number;
    String compLevel;
    String[] blueAlliance;
    String[] redAlliance;
    int progress;
    int time;

    public Match(String id, int number, String compLevel, String[] blueAlliance, String[] redAlliance, int progress, int time) {
        super(id.split("_")[0]);
        this.number = number;
        this.compLevel = compLevel;
        this.blueAlliance = blueAlliance;
        this.redAlliance = redAlliance;
        this.progress = progress;
        this.time = time;
    }

    public int getNumber() { return number; }

    public String[] getBlueAlliance() {
        return blueAlliance;
    }

    public String[] getRedAlliance() {
        return redAlliance;
    }

    public int getProgress() { return progress; }

    public int getTime() { return time; }
}
