package org.softauto.listener.server;

import java.util.concurrent.CountDownLatch;

public interface Function<T, R> {

    CountDownLatch getLock();

    R apply(T t);

    R getResult();
}
