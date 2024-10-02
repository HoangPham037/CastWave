package com.castwave.castwave.base.rx;

public class Buser {

    private String key;
    private Object values;

    public Buser(String key, Object values) {
        this.key = key;
        this.values = values;
    }

    public String getKey() {
        return key;
    }

    public Object getValues() {
        return values;
    }
}
