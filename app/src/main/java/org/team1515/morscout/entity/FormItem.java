package org.team1515.morscout.entity;

import java.util.List;

public class FormItem extends Entity {

    private String name;
    private String type;
    private String value;
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
        this.value = null;
    }

    public FormItem(String id, String name, String type, List<String> options) {
        this(id, name, type, options, -1, -1);
    }

    public FormItem(String name, String value) {
        super(null);
        this.name = name;
        this.value = value;
        this.type = null;
        this.options = null;
        this.min = -1;
        this.max = -1;
    }

    public FormItem(String name) {
        this(name, null);
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

    public String getValue() {
        return  value;
    }
}
