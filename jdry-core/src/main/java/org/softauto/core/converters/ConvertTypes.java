package org.softauto.core.converters;

public enum ConvertTypes {

    JSON ("json"),
    NONE ("none");

    private ConvertTypes(String  value) {
        this.value = value;
    }

    private final String value;


    public static ConvertTypes fromString(String text) {
        for (ConvertTypes b : ConvertTypes.values()) {
            if (b.name().equals(text)) {
                return b;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return this.value;
    }
}
