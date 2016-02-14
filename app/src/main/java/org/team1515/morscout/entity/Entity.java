package org.team1515.morscout.entity;

public abstract class Entity {
    private String id;

    public Entity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract String getName();
}
