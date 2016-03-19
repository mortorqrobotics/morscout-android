package org.team1515.morscout.entity;

public class Team extends Entity {

    int number;
    String name;
    String url;
    String location;
    String sponsors;

    public Team(String id, int number, String name, String url, String location, String sponsors) {
        super(id);
        this.number = number;
        this.name = name;
        this.url = url;
        this.location = location;
        this.sponsors = sponsors;
    }

    public Team(String id, int number, String name) {
        this(id, number, name, null, null, null);
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
}
