package org.softauto.core;

@FunctionalInterface
public interface Handler<E> {
    void handle(E var1) throws Exception;
}
