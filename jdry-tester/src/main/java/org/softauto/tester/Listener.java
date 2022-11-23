package org.softauto.tester;

import org.apache.avro.ipc.CallFuture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.core.*;
import org.softauto.listener.*;
import java.util.function.Function;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;



public  class Listener implements IListener {

    private static Logger logger = LogManager.getLogger(Listener.class);
    public static long timeOutInMin = 3;
    CountDownLatch lock = new CountDownLatch(0);
    String fqmn;
    Object result;
    Exec func;




    public Exec getFunc() {
        return func;
    }

    public Listener setFunc(Exec func) {
        this.func = func;
        return this;
    }

    public Object getResult() {
        return result;
    }

    public Listener setFqmn(String fqmn) {
        this.fqmn = fqmn;
        return this;
    }

    /*
    public <T> Listener waitTo( Handler<AsyncResult<T>> resultHandler)throws Exception{
        FunctionAfter func = new FunctionAfter(resultHandler,fqmn);
        ListenerObserver.getInstance().register(fqmn,func);
        lock = func.getLock();
        lock.await(timeOutInMin, TimeUnit.MINUTES);
        resultHandler.handle(Future.handleResult((T)func.getResult()));
        return this;
    }

     */


    public <T> Listener waitTo(Function function, Handler<AsyncResult<T>> resultHandler)throws Exception{
        FunctionBefore func = new FunctionBefore(function,fqmn);
        ListenerObserver.getInstance().register(fqmn,func);
        lock = func.getLock();
        lock.await(timeOutInMin, TimeUnit.MINUTES);
        resultHandler.handle(Future.handleResult((T)func.getResult()));
        return this;
    }

    public <T> Listener waitTo(Handler<AsyncResult<T>> resultHandler)throws Exception{
        lock = func.getLock();
        lock.await(timeOutInMin, TimeUnit.MINUTES);
        resultHandler.handle(Future.handleResult((T)func.getResult()));
        return this;
    }

    public <T> Listener waitTo(Function function, CallFuture<T> future)throws Exception{
        logger.debug("waitTo "+ fqmn);

        FunctionBefore func = new FunctionBefore(function,fqmn);
        ListenerObserver.getInstance().register(fqmn,func);
        lock = func.getLock();
        lock.await(timeOutInMin, TimeUnit.MINUTES);
        if(lock.getCount() > 0){
            logger.error("done waitTo for "+ fqmn+" no call ");
        }else {
            logger.debug("done waitTo for " + fqmn);
        }
        future.handleResult((T)func.getResult());
        return this;
    }

    public <T> Listener waitTo( CallFuture<T> future)throws Exception{
        logger.debug("waitTo "+ fqmn);
        lock = func.getLock();
        lock.await(timeOutInMin, TimeUnit.MINUTES);
        if(lock.getCount() > 0){
            logger.error("done waitTo for "+ fqmn+" no call ");
        }else {
            logger.debug("done waitTo for " + fqmn);
        }
        future.handleResult((T)func.getResult());
        return this;
    }

    public Listener waitTo(Function function)throws Exception{
        logger.debug("waitTo "+ fqmn);
        FunctionBefore func = new FunctionBefore(function,fqmn);
        ListenerObserver.getInstance().register(fqmn,func);
        lock = func.getLock();
        lock.await(timeOutInMin, TimeUnit.MINUTES);
        if(lock.getCount() > 0){
            logger.error("done waitTo for "+ fqmn+" no call ");
        }else {
            logger.debug("done waitTo for " + fqmn);
        }
        result = func.getResult();
        return this;
    }

    public Listener waitTo()throws Exception{
        logger.debug("waitTo "+ fqmn);
        lock = func.getLock();
        lock.await(timeOutInMin, TimeUnit.MINUTES);
        if(lock.getCount() > 0){
            logger.error("done waitTo for "+ fqmn+" no call ");
        }else {
            logger.debug("done waitTo for " + fqmn);
        }
        result = func.getResult();
        return this;
    }

    public <T> Listener waitToResult( Function function, CallFuture<T> future)throws Exception{
        logger.debug("waitToResult "+ fqmn);
        FunctionAfter func = new FunctionAfter(function,fqmn);
        ListenerObserver.getInstance().register(fqmn,func);
        lock = func.getLock();
        lock.await(timeOutInMin, TimeUnit.MINUTES);
        future.handleResult((T)func.getResult());
        if(lock.getCount() > 0){
            logger.error("done waitTo for "+ fqmn+" no call ");
        }else {
            logger.debug("done waitTo for " + fqmn);
        }
        return this;
    }

    public <T> Listener waitToResult(  CallFuture<T> future)throws Exception{
        logger.debug("waitToResult "+ fqmn);
        lock = func.getLock();
        lock.await(timeOutInMin, TimeUnit.MINUTES);
        future.handleResult((T)func.getResult());
        if(lock.getCount() > 0){
            logger.error("done waitTo for "+ fqmn+" no call ");
        }else {
            logger.debug("done waitTo for " + fqmn);
        }
        return this;
    }


    public <T> Listener waitToResult(Function function, Handler<AsyncResult<T>> resultHandler)throws Exception{
        logger.debug("waitToResult "+ fqmn);
        FunctionAfter func = new FunctionAfter(function,fqmn);
        ListenerObserver.getInstance().register(fqmn,func);
        lock = func.getLock();
        lock.await(timeOutInMin, TimeUnit.MINUTES);
        resultHandler.handle(Future.handleResult((T)func.getResult()));
        if(lock.getCount() > 0){
            logger.error("done waitTo for "+ fqmn+" no call ");
        }else {
            logger.debug("done waitTo for " + fqmn);
        }
        return this;
    }

    public <T> Listener waitToResult( Handler<AsyncResult<T>> resultHandler)throws Exception{
        logger.debug("waitToResult "+ fqmn);
        lock = func.getLock();
        lock.await(timeOutInMin, TimeUnit.MINUTES);
        resultHandler.handle(Future.handleResult((T)func.getResult()));
        if(lock.getCount() > 0){
            logger.error("done waitTo for "+ fqmn+" no call ");
        }else {
            logger.debug("done waitTo for " + fqmn);
        }
        return this;
    }

}