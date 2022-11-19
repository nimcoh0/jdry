package org.softauto.annotations.util;



public enum StepMode {

    STEP,
    API,
    NONE;

    public static StepMode fromString(String text) {
        for (StepMode b : StepMode.values()) {
            if (b.name().equals(text)) {
                return b;
            }
        }
        return null;
    }
}
