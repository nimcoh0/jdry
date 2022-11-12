package org.softauto.jaxrs.annotations;

public enum HttpMethod {

    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    NONE("NONE"),
    DELETE("DELETE");


    private HttpMethod(String  value) {
        this.value = value;
    }



    private final String value;


    public static HttpMethod fromString(String text) {
        for (HttpMethod b : HttpMethod.values()) {
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
