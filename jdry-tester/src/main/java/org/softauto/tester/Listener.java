package org.softauto.tester;

import org.apache.avro.ipc.CallFuture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.core.*;
import org.softauto.listener.*;
import org.softauto.service.ListenerService;

import java.util.function.Function;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;



public  class Listener implements IListener {

    private static Logger logger = LogManager.getLogger(Listener.class);
    public static long timeOutInMin = 3;
    CountDownLatch lock = new CountDownLatch(0);
    public String fqmn;
    Object result;
    Exec func;
    Class[] types;

    public Class[] getTypes() {
        return types;
    }

    public Listener setTypes(Class[] types) {
        this.types = types;
        return this;
    }

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


    public <T> Listener waitTo(Function function, Handler<AsyncResult<T>> resultHandler)throws Exception{
        Exec func = new Exec(function,fqmn);
        ListenerObserver.getInstance().register(fqmn,func);
        lock = func.getLock();
        lock.await(timeOutInMin, TimeUnit.MINUTES);
        resultHandler.handle(Future.handleResult((T)func.getResult()));
        return this;
    }



    public <T> Listener waitTo(Handler<AsyncResult<T>> resultHandler)throws Exception{
        Exec func = new Exec(fqmn);
        ListenerObserver.getInstance().register(fqmn,func);
        lock = func.getLock();
        lock.await(timeOutInMin, TimeUnit.MINUTES);
        resultHandler.handle(Future.handleResult((T)func.getResult()));
        return this;
    }


    public <T> Listener waitTo(Function function, CallFuture<T> future)throws Exception{
        logger.debug("waitTo "+ fqmn);

        Exec func = new Exec(function,fqmn);
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

        Exec func = new Exec(fqmn);
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


    public Listener waitTo(Function function)throws Exception{
        logger.debug("waitTo "+ fqmn);
        Exec func = new Exec(function,fqmn);
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
        Exec func = new Exec(fqmn);
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

    public org.softauto.tester.Listener register(Function function) throws Exception {
        ListenerObserver.getInstance().register(fqmn,function);
        return this;
    }

    public org.softauto.tester.Listener register() throws Exception {
        ListenerObserver.getInstance().register(fqmn,func);
        return this;
    }



}