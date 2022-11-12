package org.softauto.jaxrs.annotations;

public enum MediaType {

    APPLICATION_ATOM_XML("application/atom+xml"),
    APPLICATION_ATOM_XML_TYPE("application/atom+xml"),
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded"),
    APPLICATION_FORM_URLENCODED_TYPE("application/x-www-form-urlencoded"),
    APPLICATION_JSON("application/json"),
    APPLICATION_JSON_TYPE("application/json"),
    APPLICATION_OCTET_STREAM( "application/octet-stream"),
    APPLICATION_OCTET_STREAM_TYPE("application/octet-stream"),
    APPLICATION_SVG_XML("application/svg+xml"),
    APPLICATION_SVG_XML_TYPE("application/svg+xml"),
    APPLICATION_XHTML_XML("application/xhtml+xml"),
    APPLICATION_XHTML_XML_TYPE ("application/xhtml+xml"),
    APPLICATION_XML("application/xml"),
    APPLICATION_XML_TYPE("application/xml"),
    MEDIA_TYPE_WILDCARD("*"),
    MULTIPART_FORM_DATA("multipart/form-data"),
    MULTIPART_FORM_DATA_TYPE("multipart/form-data"),
    TEXT_HTML("text/html"),
    TEXT_HTML_TYPE ("text/html"),
    TEXT_PLAIN ("text/plain"),
    TEXT_PLAIN_TYPE("text/plain"),
    TEXT_XML ("text/xml"),
    TEXT_XML_TYPE  ("text/xml"),
    WILDCARD("*/*"),
    NONE("none"),
    WILDCARD_TYPE ("*/*");


    private MediaType(String  value) {
        this.value = value;
    }



    private final String value;


    public static MediaType fromString(String text) {
        for (MediaType b : MediaType.values()) {
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
