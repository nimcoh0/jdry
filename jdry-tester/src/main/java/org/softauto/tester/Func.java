package org.softauto.tester;

@FunctionalInterface
public interface Func {

    <T,R> R apply(T t);

}
