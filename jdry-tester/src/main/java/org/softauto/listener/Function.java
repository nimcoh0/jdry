package org.softauto.listener;

import java.util.concurrent.CountDownLatch;

public interface Function<T, R> {

    CountDownLatch getLock();

    R apply(T t);

    R getResult();
}
