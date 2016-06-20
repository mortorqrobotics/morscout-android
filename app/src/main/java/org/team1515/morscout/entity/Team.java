package org.team1515.morscout.entity;

public class Team extends Entity {

    private int number;
    private String name;
    private String url;
    private String location;
    private String sponsors;
    private int progress;

    public Team(String id, int number, String name, String url, String location, String sponsors) {
        super(id);
        this.number = number;
        this.name = name;
        this.url = url;
        this.location = location;
        this.sponsors = sponsors;
        this.progress = -1;
    }

    public Team(String id, int number, String name) {
        this(id, number, name, null, null, null);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSponsors() {
        return sponsors;
    }

    public void setSponsors(String sponsors) {
        this.sponsors = sponsors;
    }

    public int getProgress() { return progress; }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
