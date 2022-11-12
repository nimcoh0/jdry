package org.softauto.core;

@FunctionalInterface
public interface Action<R> {

    R exec();

}
