package org.softauto.core;

import java.util.function.Function;

@FunctionalInterface
public interface Handler1<E> {
    Function handle(E var1) throws Exception;
}
