package org.softauto.core;

/**
 * supported class type
 */
public enum ClassType {

    SINGLETON,
    INITIALIZE,
    INITIALIZE_NO_PARAM,
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
