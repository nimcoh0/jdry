package org.softauto.annotations;

public enum VerifyType {

    RESULT("waitToResult"),
    ARGS("waitTo"),
    NONE("none");

    private VerifyType(String  value) {
        this.value = value;
    }



    private final String value;


    public static VerifyType fromString(String text) {
        for (VerifyType b : VerifyType.values()) {
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
