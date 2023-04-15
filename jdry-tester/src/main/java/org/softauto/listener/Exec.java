package org.softauto.listener;

import java.util.concurrent.CountDownLatch;

public  class Exec implements Function {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Exec.class);
    CountDownLatch lock = null;
    java.util.function.Function func = null;
    String key = null;
    Object result = null;
    boolean seen = false;
    //String phase;



    @Override
    public Object getResult(){
        return result;
    }


    public Exec(java.util.function.Function o, String key){
        lock = new CountDownLatch(1);
        func = o;
        this.key = key;
        seen = false;
        logger.debug("init function after for "+ key);
    }

    @Override
    public CountDownLatch getLock(){
        return lock;
    }


    @Override
    public Object apply(Object o){
        try {
            if(!seen) {
                this.result = func.apply(o);
                logger.debug("apply function After result  " + result);
                seen = true;
            }else{
                logger.debug("message was already execute "+ key);
            }
        }catch (Exception e){
            logger.error("fail apply ",e);
        }finally {
            lock.countDown();
        }
        return result;
    }




}
