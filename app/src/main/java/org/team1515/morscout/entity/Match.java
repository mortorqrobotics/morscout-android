package org.team1515.morscout.entity;

public class Match extends Entity {

    private int number;
    private String compLevel;
    private String[] blueAlliance;
    private String[] redAlliance;
    private int progress;
    private long time;

    public Match(String id, int number, String compLevel, String[] blueAlliance, String[] redAlliance, long time) {
        super(id.split("_")[0]);
        this.number = number;
        this.compLevel = compLevel;
        this.blueAlliance = blueAlliance;
        this.redAlliance = redAlliance;
        this.time = time*1000;
        this.progress = -1;
    }

    public int getNumber() { return number; }

    public void setNumber(int number) {
        this.number = number;
    }

    public String[] getBlueAlliance() {
        return blueAlliance;
    }

    public void setBlueAlliance(String[] blueAlliance) {
        this.blueAlliance = blueAlliance;
    }

    public String[] getRedAlliance() {
        return redAlliance;
    }

    public void setRedAlliance(String[] redAlliance) {
        this.redAlliance = redAlliance;
    }

    public int getProgress() { return progress; }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getTime() { return time; }

    public void setTime(long time) {
        this.time = time;
    }
}
