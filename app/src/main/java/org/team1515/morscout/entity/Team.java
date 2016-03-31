package org.team1515.morscout.entity;

public class Team extends Entity {

    int number;
    String name;
    String url;
    String location;
    String sponsors;
    int progress;

    public Team(String id, int number, String name, String url, String location, String sponsors, int progress) {
        super(id);
        this.number = number;
        this.name = name;
        this.url = url;
        this.location = location;
        this.sponsors = sponsors;
        this.progress = progress;
    }

    public Team(String id, int number, String name, int progress) {
        this(id, number, name, null, null, null, progress);
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getLocation() {
        return location;
    }

    public String getSponsors() {
        return sponsors;
    }

    public int getProgress() { return progress; }
}
