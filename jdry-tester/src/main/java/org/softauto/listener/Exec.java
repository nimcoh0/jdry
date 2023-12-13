package org.softauto.listener;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import java.util.concurrent.CountDownLatch;

public  class Exec implements Function {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Exec.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    CountDownLatch lock = null;
    java.util.function.Function func = null;
    java.util.function.Consumer cons = null;

    String key = null;
    Object result = null;
    boolean seen = false;

    @Override
    public Object getResult(){
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Exec(java.util.function.Function o, String key){
        lock = new CountDownLatch(1);
        func = o;
        this.key = key;
        seen = false;
        logger.debug(JDRY,"init function  for "+ key);
    }



    public Exec(java.util.function.Consumer<Object> o, String key){
        lock = new CountDownLatch(1);
        cons = o;
        this.key = key;
        seen = false;
        logger.debug(JDRY,"init function for "+ key);
    }

    public Exec(String key){
        lock = new CountDownLatch(1);
        this.key = key;
        seen = false;
        logger.debug(JDRY,"init function  for "+ key);
    }

    @Override
    public CountDownLatch getLock(){
        return lock;
    }


    @Override
    public Object apply(Object o){
        try {
            if(!seen) {
                if (func != null) {
                    this.result = func.apply(o);
                } else if (cons != null) {
                    cons.accept(o);
                }else {
                    this.result = o;
                }
                logger.debug(JDRY,"apply function After result  " + result);
                seen = true;
            }else{
                logger.debug(JDRY,"message was already execute "+ key);
            }

        }catch (Exception e){
            logger.error(JDRY,"fail apply ",e);
        }finally {
            lock.countDown();
        }
        return result;
    }




}
