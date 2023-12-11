package org.softauto.core;

import java.util.function.Function;

public interface AsyncResult<T>{
    T result();

    Object[] arguments();

    Class[] argumentsType();

    Throwable cause();

    boolean succeeded();

    boolean failed();

    default <U> AsyncResult<U> map(final Function<T, U> mapper) {
        if (mapper == null) {
            throw new NullPointerException();
        } else {
            return new AsyncResult<U>() {
                public U result() {
                    return this.succeeded() ? mapper.apply(AsyncResult.this.result()) : null;
                }

                public Throwable cause() {
                    return AsyncResult.this.cause();
                }

                public boolean succeeded() {
                    return AsyncResult.this.succeeded();
                }

                public boolean failed() {
                    return AsyncResult.this.failed();
                }

                public Object[] arguments() {
                    return AsyncResult.this.arguments();
                }

                public Class[] argumentsType() {
                    return AsyncResult.this.argumentsType();
                }
            };
        }
    }

    default <V> AsyncResult<V> map(V value) {
        return this.map((t) -> {
            return value;
        });
    }

    default <V> AsyncResult<V> mapEmpty() {
        return (AsyncResult<V>) this.map((Object)null);
    }

    default AsyncResult<T> otherwise(final Function<Throwable, T> mapper) {
        if (mapper == null) {
            throw new NullPointerException();
        } else {
            return new AsyncResult<T>() {
                public T result() {
                    if (AsyncResult.this.succeeded()) {
                        return AsyncResult.this.result();
                    } else {
                        return AsyncResult.this.failed() ? mapper.apply(AsyncResult.this.cause()) : null;
                    }
                }

                public Throwable cause() {
                    return null;
                }

                public boolean succeeded() {
                    return AsyncResult.this.succeeded() || AsyncResult.this.failed();
                }

                public boolean failed() {
                    return false;
                }

                public Object[] arguments() {
                    return new Object[]{};
                }

                public Class[] argumentsType() {
                    return new Class[]{};
                }
            };
        }
    }

    default AsyncResult<T> otherwise(T value) {
        return this.otherwise((err) -> {
            return value;
        });
    }

    default AsyncResult<T> otherwiseEmpty() {
        return this.otherwise((err) -> {
            return null;
        });
    }

    default AsyncResult<T> setArguments(Object[] arg) {
       //return  (AsyncResult<T>)new org.softauto.serializer.CallFuture().handleArguments(arg);
        return  (AsyncResult<T>)new CallFuture().handleArguments(arg);

    }



}
