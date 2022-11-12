package org.softauto.core;


public enum AssertType {

    
    AssertTrue("Assert.assertTrue"),
    AssertFalse("Assert.assertTrue"),
    AssertEquals("Assert.assertEquals"),
    AssertNotEquals("Assert.assertNotEquals"),
    NONE("none");

    private AssertType(String  value) {
        this.value = value;
    }

    private final String value;


    public static AssertType fromString(String text) {
        for (AssertType b : AssertType.values()) {
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
