package org.softauto.annotations;

/**
 * supported class type
 */
public enum ClassType {

    SINGLETON,
    INITIALIZE_EVERY_TIME,
    NONE;

    private ClassType() {
    }



    public static ClassType fromString(String text) {
        for (ClassType b : ClassType.values()) {
            if (b.name().equals(text)) {
                return b;
            }
        }
        return null;
    }
}
