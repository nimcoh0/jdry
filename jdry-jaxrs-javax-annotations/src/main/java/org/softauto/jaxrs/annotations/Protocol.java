package org.softauto.jaxrs.annotations;

public enum Protocol {

    HTTP("HTTP"),
    HTTPS("HTTPS");


    private Protocol(String  value) {
        this.value = value;
    }



    private final String value;


    public static Protocol fromString(String text) {
        for (Protocol b : Protocol.values()) {
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
