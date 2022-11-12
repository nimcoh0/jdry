package org.softauto.tester;

import org.apache.avro.ipc.CallFuture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.core.IListener;
import org.softauto.espl.ExpressionBuilder;
import org.softauto.listener.server.FunctionAfter;
import org.softauto.listener.server.FunctionBefore;
import org.softauto.listener.server.FunctionBeforeWithCondition;
import org.softauto.listener.server.ListenerObserver;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;


public  class Listener implements IListener {

        private static Logger logger = LogManager.getLogger(Listener.class);
        public static long timeOutInMin = 3;
        CountDownLatch lock = new CountDownLatch(0);
        String fqmn;
        static Object result;

        org.softauto.listener.server.Function func;

        public org.softauto.listener.server.Function getFunc(){
               return func;
           }

        public CountDownLatch getLock(){
               return lock;
           }

        public Object getResult(){
                return result;
            }



            public Listener getResult(CallFuture<Object> future){
                future.handleResult(result);
                return this;
            }



            public static void setTimeOut(long min){
                timeOutInMin = min;
            }

            public static Listener resetListeners()throws Exception{
                SystemState.getInstance().resetListeners();
                ListenerObserver.getInstance().reset();
                logger.debug("reset Listeners successfully");
                return  new Listener();
            }

            public static Listener removeListener(String fqmn, Class...types)throws Exception{
                SystemState.getInstance().removeListener(fqmn,types);
                ListenerObserver.getInstance().unRegister(fqmn);
                logger.debug("remove Listener successfully "+ fqmn);
                return  new Listener();
            }


            public static Listener addListener(String fqmn, Class...types)throws Exception{
                SystemState.getInstance().addListener(fqmn,types);
                logger.debug("add Listener successfully "+ fqmn+ " types "+ Arrays.toString(types));
                return  new Listener();
            }


            public static Listener addListener(String fqmn,Object[] value, Class...types)throws Exception{
                SystemState.getInstance().addListener(fqmn,value,types);
                logger.debug("add Listener im mock mode successfully "+ fqmn+ " types "+ Arrays.toString(types));
                return  new Listener();
            }


            public static Listener addListeners(HashMap<String,Class[]> listeners)throws Exception{
                listeners.forEach((fqmn,types)-> {
                    try {
                        SystemState.getInstance().addListener(fqmn, types);
                        logger.debug("add Listener successfully "+ fqmn+ " types "+ Arrays.toString(types));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                return  new Listener();
            }

            public Function ff()throws Exception{
                return new Function() {
                    @Override
                    public Object apply(Object o) {
                        return o;
                    }
                };
            }

            public Listener mock(Object result)throws Exception{
                logger.debug("mock "+ this.fqmn);
                this.result = result;
                return this;
            }

            public Listener waitTo(String fqmn)throws Exception{
                return waitTo(fqmn,ff());
            }

            public Listener waitTo(String fqmn, Function function)throws Exception{
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

            public <T> Listener waitTo(String fqmn, CallFuture<T> future)throws Exception{
                return waitTo(fqmn,ff(),future);
            }


            public <T> Listener waitTo(String fqmn, Function function, CallFuture<T> future)throws Exception{
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








            public Listener waitUntil(String fqmn,  ExpressionBuilder exp)throws Exception{
                return waitUntil(fqmn,ff(),exp);
            }

            public Listener waitUntil(String fqmn, Function function, ExpressionBuilder exp)throws Exception{
                logger.debug("waitUntil "+ fqmn+ " exp "+ exp.toJson());
                FunctionBeforeWithCondition func = new FunctionBeforeWithCondition(function,fqmn,exp);
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

            public <T> Listener waitUntil(String fqmn, ExpressionBuilder exp, CallFuture<T> future)throws Exception{
                return waitUntil(fqmn,ff(),exp,future);
            }

            public <T> Listener waitUntil(String fqmn, Function function, ExpressionBuilder exp, CallFuture<T> future)throws Exception{
                logger.debug("waitUntil "+ fqmn+ " exp "+ exp.toJson());
                FunctionBeforeWithCondition func = new FunctionBeforeWithCondition(function,fqmn,exp);
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

            public Listener waitToResult(String fqmn)throws Exception{
                return waitToResult(fqmn,ff());
            }

            public Listener waitToResult(String fqmn, Function function)throws Exception{
                logger.debug("waitToResult "+ fqmn);
                FunctionAfter func = new FunctionAfter(function,fqmn);
                ListenerObserver.getInstance().register(fqmn,func);
                lock = func.getLock();
                lock.await(timeOutInMin, TimeUnit.MINUTES);
                result = func.getResult();
                if(lock.getCount() > 0){
                    logger.error("done waitTo for "+ fqmn+" no call ");
                }else {
                    logger.debug("done waitTo for " + fqmn);
                }
                return this;
            }

            public <T> Listener waitToResult(String fqmn, CallFuture<T> future)throws Exception{
                return waitToResult(fqmn,ff(),future);
            }



            public <T> Listener waitToResult(String fqmn, Function function, CallFuture<T> future)throws Exception{
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





}