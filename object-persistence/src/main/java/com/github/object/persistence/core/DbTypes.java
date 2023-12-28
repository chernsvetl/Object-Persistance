package com.github.object.persistence.core;

public enum DbTypes {
    RELATIONAL("RELATIONAL");

    public final String typeName;

    DbTypes(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public static DbTypes getType(String typeName) {
        for (DbTypes type : DbTypes.values()) {
            if (type.typeName.equals(typeName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported DB type");
    }
}
