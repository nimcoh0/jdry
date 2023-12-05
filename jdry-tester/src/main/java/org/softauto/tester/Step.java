/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package org.softauto.tester;

import org.apache.avro.ipc.CallFuture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.core.*;
import org.softauto.espl.Espl;
import org.softauto.plugin.ProviderManager;
import org.softauto.plugin.api.Provider;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


public class Step implements IStep{

    private static Logger logger = LogManager.getLogger(Step.class);
    CallFuture<Object> future = null;
    protected HashMap<String,Object> callOptions = null;

    org.apache.avro.ipc.Callback<Object> callback;

    Object result;

    String transceiver;

    String fqmn;

    Object[] args;

    Class[] types;

    Object expected;

    boolean state;

    public Step(){

    };




    public <T> void isSuccesses(Handler<AsyncResult<T>> resultHandler){
            try {
                resultHandler.handle(Future.handleResult((T) result));
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public boolean isSuccesses(){
        Object o = Espl.getInstance().addProperty("result",result).evaluate(expected.toString());
        if(o != null && o instanceof Boolean){
            return true;
        }
      return false;
    }

    public <T> boolean isFail(Handler<AsyncResult<T>> resultHandler){
        if(!state){
            try {
                resultHandler.handle(Future.handleResult((T) result));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean isFail(){
        Object o = Espl.getInstance().addProperty("result",result).evaluate(expected.toString());
        if(o == null ){
            return true;
        }
        return false;
    }

    public Step setResult(Object result) {
        this.result = result;
        return this;
    }

    public Step setCallOptions(HashMap<String, Object> callOptions) {
        this.callOptions = callOptions;
        return this;
    }


    public Step setTransceiver(String transceiver) {
        this.transceiver = transceiver;
        return this;
    }


    public <T> T setClientBuilder(T clientBuilder) {
        //this.clientBuilder = clientBuilder;
        return clientBuilder;
    }

    public <T> T setTransceiver(String pluginName, T transceiverType) {
        this.transceiver = pluginName;
        Provider provider = ProviderManager.provider(transceiver).create();
        return (T)provider;
    }


    public Step(CallOptions callOptions){
        this.callOptions = callOptions.getOptions();
    }

    public <T> T get_Result() throws Exception{
            try {
                if (future != null) {
                    if (!future.isDone()) {
                        logger.debug("waiting to future to be done");
                        future.await();
                    }
                    if(TestContext.getScenario().getState().equals(ScenarioLifeCycle.START.name())) {
                        logger.debug("successfully get_Result() ");
                        return (T) future.get();
                    }
                }else {
                    if(TestContext.getScenario().getState().equals(ScenarioLifeCycle.START.name())) {
                        return (T) result;
                    }
                }

            }catch (Exception e){
                 logger.error("fail get_Result() "+ e);
                 throw new Exception("fail get_Result() "+ e);
             }
            return null;
        }



    public Step(String fqmn, Object[] args, Class[] types, String transceiver, HashMap<String, Object> callOptions,String scenarioId)throws Exception{
        future = new CallFuture<>();
        logger.debug("invoking " +fqmn);
        callOptions.put("scenarioId",scenarioId);
        new InvocationHandler().invoke(fqmn,args,types,future,transceiver,callOptions);
    }

    /*
    public Step(String fqmn, Object[] args, Class[] types, String transceiver, HashMap<String, Object> callOptions)throws Exception{
        future = new CallFuture<>();
        logger.debug("invoking " +fqmn);
        new InvocationHandler().invoke(fqmn,args,types,future,transceiver,callOptions);
    }

     */

    public <T> Step(String fqmn, Object[] args, Class[] types, String transceiver,HashMap<String, Object> callOptions,String scenarioId, CallFuture<T> future)throws Exception{

            logger.debug("invoking " + fqmn);
            callOptions.put("scenarioId", scenarioId);
            new InvocationHandler().invoke(fqmn, args, types, future, transceiver, callOptions);

    }

    /*
    public <T> Step(String fqmn, Object[] args, Class[] types, String transceiver,HashMap<String, Object> callOptions, org.apache.avro.ipc.Callback<T> future)throws Exception{
        logger.debug("invoking " +fqmn);
        new InvocationHandler().invoke(fqmn,args,types,future,transceiver,callOptions);
    }

     */



    public <T> Step(String fqmn, Object[] args, Class[] types, String transceiver,HashMap<String, Object> callOptions,org.apache.avro.ipc.Callback<Object> future)throws Exception{

            logger.debug("invoking " + fqmn);
            this.fqmn = fqmn;
            this.args = args;
            this.types = types;
            this.callback = future;
            this.setTransceiver(transceiver);
            this.setCallOptions(callOptions);

        //new InvocationHandler().invoke(fqmn,args,types,future,transceiver,callOptions);
    }

    public <T> Step(String fqmn, Object[] args, Class[] types, String transceiver,HashMap<String, Object> callOptions)throws Exception{

            logger.debug("invoking " + fqmn);
            this.fqmn = fqmn;
            this.args = args;
            this.types = types;
            this.setTransceiver(transceiver);
            this.setCallOptions(callOptions);

        //this.callback = future;
        //new InvocationHandler().invoke(fqmn,args,types,future,transceiver,callOptions);
    }

    public <RespT> Step invoke() throws Exception {
        if (this.callback == null) {
            result = new InvocationHandler().invoke(fqmn, args, types, transceiver, callOptions);
        } else {
            new InvocationHandler().invoke(fqmn, args, types, callback, transceiver, callOptions);
        }
        return this;
    }

    public Object getExpected() {
        return expected;
    }

    public Step setExpected(Object expected) {
        this.expected = expected;
        return this;
    }

    public Step setExpected(String expression) {
        this.expected = expression;
        return this;
    }

   /*
    public <RespT> Step invoke() throws Exception {

            if (this.callback == null) {
                result = new InvocationHandler().invoke(fqmn, args, types, transceiver, callOptions);
            } else {
                new InvocationHandler().invoke(fqmn, args, types, callback, transceiver, callOptions);

                //String newContent = new String(((ByteBuffer)((CallFuture)callback).getResult()).array(), StandardCharsets.UTF_8);
                //result =  new ObjectMapper().readValue(newContent,Object.class);
            }

      //Object o = new StepService().setCallOptions(callOptions).setTransceiver(transceiver).invokeUnaryMethod(fqmn,types,args);
      //System.out.println(o);
      return this;
      //CallFuture<Object> callFuture = new CallFuture<>();
      //new InvocationHandler().invoke(fqmn,args,types,callFuture,transceiver,callOptions);
      //return callFuture.get();
     // return   new Step(fqmn, args, types, transceiver, callOptions, callback);
    }
    */

    public Step(String fqmn, Object[] args, Class[] types, String transceiver,CallOptions callOptions,String scenarioId)throws Exception{

            future = new CallFuture<>();
            logger.debug("invoking " + fqmn);
            callOptions.getOptions().put("scenarioId", scenarioId);
            new InvocationHandler().invoke(fqmn, args, types, future, transceiver, callOptions.getOptions());

    }

    public Step(String fqmn, Object[] args, Class[] types, String transceiver,CallOptions callOptions)throws Exception{

            future = new CallFuture<>();
            logger.debug("invoking " + fqmn);
            new InvocationHandler().invoke(fqmn, args, types, future, transceiver, callOptions.getOptions());

    }


    public Step(String fqmn, Object[] args, Class[] types, String transceiver) throws Exception {

            this.future = new CallFuture();
            logger.debug("invoking " + fqmn);
            (new InvocationHandler()).invoke(fqmn, args, types, this.future, transceiver);

    }


    public <T> Step(String fqmn, Object[] args, Class[] types, String transceiver, CallFuture<T> future)throws Exception{

            logger.debug("invoking " + fqmn);
            new InvocationHandler().invoke(fqmn, args, types, future, transceiver);

    }



        public Step then(IListener o)throws Exception{
            return this;
        }

        public Step then(IListener...o)throws Exception{
            future.handleResult(future.getResult());
            return this;
        }


        public void then(String expression)throws Exception{
            Object result = Espl.getInstance().evaluate(expression);
            future.handleResult(result);
        }

        public void then(Supplier supplier)throws Exception{
            future.handleResult(supplier.get());
        }

        public void then(Consumer consumer,Object value)throws Exception{
            consumer.accept(value);
            future.handleResult("ok");
        }

        public void then(Function function,Object value)throws Exception{
            future.handleResult(function.apply(value));
        }

    public static long timeOutInMin = 3;



}