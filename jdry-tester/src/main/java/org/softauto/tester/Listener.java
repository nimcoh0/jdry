package org.softauto.tester;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.core.*;
import org.softauto.listener.*;
import java.util.function.Function;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public  class Listener implements IListener {

    private static Logger logger = LogManager.getLogger(Listener.class);
    private static final Marker JDRY = MarkerManager.getMarker("JDRY");
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

    public Listener waitTo(java.util.function.Consumer<Object> consumer)throws Exception{
        logger.debug(JDRY,"waitTo "+ fqmn);
        Exec func = new Exec(consumer,fqmn);
        ListenerObserver.getInstance().register(fqmn,func);
        lock = func.getLock();
        lock.await(timeOutInMin, TimeUnit.MINUTES);
        if(lock.getCount() > 0){
            logger.error(JDRY,"done waitTo for "+ fqmn+" no call ");
        }else {
            logger.debug(JDRY,"done waitTo for " + fqmn);
        }
        return this;
    }

    public Listener waitTo(Function function)throws Exception{
        logger.debug(JDRY,"waitTo "+ fqmn);
        Exec func = new Exec(function,fqmn);
        ListenerObserver.getInstance().register(fqmn,func);
        lock = func.getLock();
        lock.await(timeOutInMin, TimeUnit.MINUTES);
        if(lock.getCount() > 0){
            logger.error(JDRY,"done waitTo for "+ fqmn+" no call ");
        }else {
            logger.debug(JDRY,"done waitTo for " + fqmn);
        }
        result = func.getResult();
        return this;
    }



    public Listener waitTo()throws Exception{
        logger.debug(JDRY,"waitTo "+ fqmn);
        Exec func = new Exec(fqmn);
        ListenerObserver.getInstance().register(fqmn,func);
        lock = func.getLock();
        lock.await(timeOutInMin, TimeUnit.MINUTES);
        if(lock.getCount() > 0){
            logger.error(JDRY,"done waitTo for "+ fqmn+" no call ");
        }else {
            logger.debug(JDRY,"done waitTo for " + fqmn);
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