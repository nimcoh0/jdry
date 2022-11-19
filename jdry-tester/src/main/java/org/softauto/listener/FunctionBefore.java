package org.softauto.listener;

import java.util.concurrent.CountDownLatch;

public  class FunctionBefore implements Function{

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(FunctionBefore.class);
    CountDownLatch lock = null;
    java.util.function.Function func = null;
    String key = null;
    Object result = null;
    boolean seen = false;


    @Override
    public Object getResult(){
        return result;
    }


    public FunctionBefore(java.util.function.Function o, String key){
        lock = new CountDownLatch(1);
        func = o;
        this.key = key;
        seen = false;
        logger.debug("init function before for "+ key);
    }


    public FunctionBefore(String key){
        lock = new CountDownLatch(1);
        this.key = key;
        seen = false;
        logger.debug("init function before for "+ key);
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
                logger.debug("apply function before result  " + result);
                seen = true;
                //return result;
            }else{
                logger.debug("message was already execute "+ key);
            }
        }catch (Exception e){
            logger.error("fail apply ",e);
        }finally {
            lock.countDown();
            //ListenerObserver.getInstance().unRegister(key);
            //logger.debug("function before for "+ key + " remove");
        }
        return result;
    }




}
