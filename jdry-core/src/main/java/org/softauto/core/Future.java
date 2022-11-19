package org.softauto.core;




public interface Future<T> extends AsyncResult<T>, java.util.concurrent.Future<T>{

    static <T> Future<T> handleResult(T result) {
        return (Future)(result == null ? new CallFuture() : new CallFuture().handleResult(result));
    }

    static <T> Future<T>  handleError(Throwable error){
        return (Future<T>) new CallFuture().handleError(error);
    }

    static <T> Future<T>  handleArguments(Object[] arguments) {
        return (Future<T>) new CallFuture().handleArguments(arguments);
    }

    static <T> Future<T>  handleArgumentsType(Class[] argumentsType) {
        return (Future<T>) new CallFuture().handleArgumentsType(argumentsType);
    }

}
