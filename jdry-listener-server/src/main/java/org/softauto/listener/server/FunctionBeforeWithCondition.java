package org.softauto.listener.server;

import org.softauto.espl1.EvulExp;
import org.softauto.espl1.ExpressionBuilder;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public  class FunctionBeforeWithCondition implements Function{

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(FunctionBeforeWithCondition.class);
    CountDownLatch lock = null;
    java.util.function.Function func = null;
    String key = null;
    Object result = null;
    boolean seen = false;
    ExpressionBuilder exp;


    @Override
    public Object getResult(){
        return result;
    }


    public FunctionBeforeWithCondition(java.util.function.Function o, String key,ExpressionBuilder exp){
        lock = new CountDownLatch(1);
        func = o;
        this.key = key;
        seen = false;
        this.exp = exp;
        logger.debug("init function before for "+ key);
    }

    @Override
    public CountDownLatch getLock(){
        return lock;
    }

    @Override
    public Object apply(Object o){
        HashMap<String,Object> hm = new HashMap<>();
        hm.put(((Object[])o)[0].getClass().getSimpleName().toLowerCase(),((Object[])o)[0]);
        if(new EvulExp().seContexts(hm).setExp(exp).evaluate() && !seen) {
            try {
                    this.result = func.apply(o);
                    logger.debug("apply function before result  " + result);
                    seen = true;
                    return result;
            } catch (Exception e) {
                logger.error("fail apply ", e);
            } finally {
                lock.countDown();
                //ListenerObserver.getInstance().unRegister(key);
                //logger.debug("function before for "+ key + " remove");
            }
        }
        return null;
    }




}
