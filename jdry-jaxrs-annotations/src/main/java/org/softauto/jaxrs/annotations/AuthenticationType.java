package org.softauto.jaxrs.annotations;

public enum AuthenticationType {

    BASIC("BASIC"),
    DIGEST("DIGEST"),
    CERT("CERT"),
    OAUTH2("OAUTH2"),
    JWT("JWT"),
    NONE("NONE");

    private AuthenticationType(String  value) {
        this.value = value;
    }



    private final String value;


    public static AuthenticationType fromString(String text) {
        for (AuthenticationType b : AuthenticationType.values()) {
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
