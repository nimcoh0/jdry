package org.softauto.listener.manager;

import org.softauto.annotations.ListenerType;
import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.core.ScenarioLifeCycle;
import org.softauto.core.StepLifeCycle;
import org.softauto.listener.ListenerService;
import org.softauto.listener.impl.Threadlocal;
import org.softauto.serializer.Serializer;
import org.softauto.serializer.service.Message;
import org.softauto.serializer.service.MessageBuilder;
import org.softauto.system.Scenarios;

import java.util.HashMap;


public class ListenerServiceImpl implements ListenerService {


    private  final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ListenerServiceImpl.class);

    private HashMap<String,Object> getConfiguration(String scenarioId){
        if(scenarioId != null) {
           return Scenarios.getScenario(scenarioId).getConfiguration();
        }
        return null;
    }

    private String getScenarioId(){
        if(Threadlocal.getInstance().has("scenarioId")){
            return Threadlocal.getInstance().get("scenarioId");
        }
        return null;
    }

    @Override
    public Object[] executeBefore(String methodName, Object[] args, Class[] types) throws Exception {
        Object result = null;
        try {
            String scenarioId = getScenarioId();
            if(Scenarios.getScenario(scenarioId).getScenarioState().equals(ScenarioLifeCycle.START)) {
               // if(Scenarios.getScenario(scenarioId).isListenerExist(methodName)) {
                    HashMap<String, Object> configuration = getConfiguration(scenarioId);
                    if (configuration != null) {
                        Serializer serializer = new Serializer().setHost(configuration.get(Context.TEST_MACHINE).toString()).setPort(Integer.valueOf(configuration.get(Context.LISTENER_PORT).toString())).build();
                        Message message = MessageBuilder.newBuilder().setState(ListenerType.BEFORE.name()).setDescriptor(methodName).setArgs(args).setTypes(types).build().getMessage();
                        result = serializer.write(message);
                        logger.debug("send message successfully " + methodName);
                    }
               // }
            }

        } catch (Exception e) {
            if (e.getCause().toString().contains("UNAVAILABLE")) {
                logger.debug("fail on UNAVAILABLE ", e);
                return (new Object[]{});
            }
            logger.debug("send message "+methodName+" fail  ",e );
        }
        if(result == null){
            logger.debug("returning from rexecuteBefore" );
            return (new Object[]{});
        }
        if(result instanceof Object[]){
            logger.debug("returning from rexecuteBefore" );
            return (Object[])result;
        }
        logger.debug("returning from rexecuteBefore" );
        return new Object[]{result};
    }

    @Override
    public  void executeAfter(String methodName, Object[] args, Class[] types) throws Exception {

        try {
            String scenarioId = getScenarioId();
            //if(TestContext.getTestState().equals(TestLifeCycle.START)) {
            if(Scenarios.getScenario(scenarioId).getScenarioState().equals(ScenarioLifeCycle.START)) {
               // if(Scenarios.getScenario(scenarioId).isListenerExist(methodName)) {
                    HashMap<String, Object> configuration = getConfiguration(scenarioId);
                    if (configuration != null) {
                        Serializer serializer = new Serializer().setHost(Configuration.get(Context.TEST_MACHINE).asString()).setPort(Configuration.get(Context.LISTENER_PORT).asInteger()).build();
                        Message message = MessageBuilder.newBuilder().setState(ListenerType.AFTER.name()).setDescriptor(methodName).setArgs(args).setTypes(types).build().getMessage();
                        serializer.write(message);
                        logger.debug("send message successfully " + methodName);
                    }
               // }
            }
        } catch (Exception e) {
            if (e.getCause().toString().contains("UNAVAILABLE")) {
                logger.debug("fail on UNAVAILABLE ", e);

            }
            logger.debug("send message "+methodName+" fail  ",e );
        }
        logger.debug("returning from executeAfter" );

    }

    @Override
    public  Object executeException(String methodName, Object[] args, Class[] types) throws Exception {
        Object result = null;
        try {
            String scenarioId = getScenarioId();
           // if(TestContext.getTestState().equals(TestLifeCycle.START)) {
            if(Scenarios.getScenario(scenarioId).getScenarioState().equals(ScenarioLifeCycle.START)) {
                HashMap<String,Object> configuration = getConfiguration(scenarioId);
                if(configuration != null) {
                    Serializer serializer = new Serializer().setHost(Configuration.get(Context.TEST_MACHINE).asString()).setPort(Configuration.get(Context.LISTENER_PORT).asInteger()).build();
                    Message message = MessageBuilder.newBuilder().setState("Exception").setDescriptor(methodName).setArgs(args).setTypes(types).build().getMessage();
                    result = serializer.write(message);
                    logger.debug("send message successfully " + methodName);
                }
            }
        } catch (Exception e) {
            //if (e.getCause().toString().contains("UNAVAILABLE")) {
             //   logger.debug("fail on UNAVAILABLE ", e);

            //}
            logger.debug("send message "+methodName+" fail  ",e );
        }
        logger.debug("returning from Exception" );
        return result;
    }

}
