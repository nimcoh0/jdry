package org.softauto.analyzer.core.utils;

/**
 * supported class type
 */
public enum ClassType {

    SINGLETON,
    INITIALIZE_IF_NOT_EXIST,
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
