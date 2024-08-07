package org.softauto.signature;

public interface ResultInterface<T,R> {

    ResultInterface apply(T t);

    R getResult();
}
