package org.softauto.analyzer.core.utils;

@FunctionalInterface
public interface Handler<E,R> {
    R handle(E var1) throws Exception;
}
