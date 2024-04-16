package org.softauto.utils;

@FunctionalInterface
public interface Function<T, R>  {

    R apply(T t);
}
