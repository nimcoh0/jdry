package org.softauto.jaxrs.annotations;



public enum Mode {

    AUTH,
    NONE;

    public static Mode fromString(String text) {
        for (Mode b : Mode.values()) {
            if (b.name().equals(text)) {
                return b;
            }
        }
        return null;
    }
}
