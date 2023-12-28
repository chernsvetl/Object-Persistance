package com.github.object.persistence.session.impl;

public class NullWrapper {
    private final Class<?> type;

    public NullWrapper(Class<?> type) {
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }
}
