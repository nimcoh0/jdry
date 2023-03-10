package org.softauto.core;

public interface Iprovider<R,T> {

    R apply(R result,T args);
}
