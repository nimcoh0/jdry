package org.softauto.serializer.service;

public enum MessageType {

    METHOD,
    LISTENER,
    NONE;

    public static MessageType fromString(String text) {
        for (MessageType b : MessageType.values()) {
            if (b.name().toLowerCase().equals(text) || b.name().equals(text)) {
                return b;
            }
        }
        return null;
    }
}
