package org.softauto.jaxrs.util;

@FunctionalInterface
public interface Handler<E> {
    void handle(E var1) throws Exception;
}
