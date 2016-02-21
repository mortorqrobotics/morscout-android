package org.team1515.morscout.entity;

import java.util.List;

public class FormItem extends Entity {

    private String name;
    private String type;
    private List<String> options;

    public FormItem(String id, String name, String type, List<String> options) {
        super(id);
        this.name = name;
        this.type = type;
        this.options = options;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public List<String> getOptions() {
        return options;
    }
}
