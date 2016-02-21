package org.team1515.morscout.entity;

import java.util.List;

public class FormItem extends Entity {

    private String name;
    private String type;
    private List<String> options;
    private int min;
    private int max;

    public FormItem(String id, String name, String type, List<String> options, int min, int max) {
        super(id);
        this.name = name;
        this.type = type;
        this.options = options;
        this.min = min;
        this.max = max;
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

    public int getMin() { return min; }

    public int getMax() { return max; }
}
