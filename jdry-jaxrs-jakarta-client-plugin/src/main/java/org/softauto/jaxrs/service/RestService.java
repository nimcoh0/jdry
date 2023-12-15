package org.softauto.jaxrs.service;


import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.core.Utils;
import org.softauto.jaxrs.JerseyHelper;

import java.net.URI;
import java.util.HashMap;


public class RestService {

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    private static TestDefinition testDefinition = null;
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(RestService.class);

    public static TestDefinition getServiceDefinition(){
         return testDefinition;
    }

    public static <T> TestDefinition createTestDefinition(String stepName, Object[] args,Class[] types, HashMap<String,Object> callOptions,String scenarioId) {
        return _createTestDefinition(stepName,args,types,callOptions,scenarioId);
    }

    public static <T> TestDefinition createTestDefinition(String stepName, Object[] args) {
        return _createTestDefinition(stepName,args,null,null,null);
    }

    private static <T> TestDefinition _createTestDefinition(String stepName, Object[] args,Class[] types,HashMap<String,Object> callOptions,String scenarioId) {
        TestDefinition.Builder testDefinitionBuilder = null;
        try {
            TestDescriptor testDescriptor = TestDescriptor.create(stepName);
            testDefinitionBuilder = TestDefinition.builder(testDescriptor);
            testDefinitionBuilder.addStep(testDescriptor.getStep(stepName,args,types,callOptions,scenarioId),ServiceCaller.call());

        }catch (Exception e){
            e.printStackTrace();
        }
        return testDefinition = testDefinitionBuilder.build();
    }



    public static class GETMethodHandler implements ServiceCaller.UnaryClass  {

        @Override
        public <T> T invoke(IStepDescriptor stepDescriptor, Object[] args) {
            try {
                stepDescriptor.setArgs(args);
                String scenarioId = stepDescriptor.getScenarioId();
                MediaType produce = stepDescriptor.getProduce();
                Client client = stepDescriptor.getClient();
                ChannelDescriptor channel = stepDescriptor.getChannel();
                MultivaluedMap<String, Object> headers = stepDescriptor.getHeaders();
                URI uri = channel.getUri();
                Cookie cookie = stepDescriptor.getCookie();
                Class returnType = stepDescriptor.getReturnType();
                logger.debug(JDRY,"invoke GET for "+ uri);
                return (T) new JerseyHelper().setClient(client).get(uri.toString(), produce.toString(), headers, returnType,cookie,scenarioId);
            }catch (Exception e){
                logger.error(JDRY,"fail invoke GET for uri "+ stepDescriptor.getChannel().getUri().getPath()+ " with args "+ Utils.result2String((Object[])args),e);
            }
            return null;
        }
    }

    public static class POSTMethodHandler implements ServiceCaller.UnaryClass  {



        @Override
        public <T> T invoke(IStepDescriptor stepDescriptor, Object[] args) {
            try {
                String scenarioId = stepDescriptor.getScenarioId();
                stepDescriptor.setArgs(args);
                MediaType produce = stepDescriptor.getConsume();
                Client client = stepDescriptor.getClient();
                ChannelDescriptor channel = stepDescriptor.getChannel();
                MultivaluedMap<String, Object> headers = stepDescriptor.getHeaders();
                Entity<?> entity = stepDescriptor.getEntity();
                URI uri = channel.getUri();
                Cookie cookie = stepDescriptor.getCookie();
                Class returnType = stepDescriptor.getReturnType();
                logger.debug(JDRY,"invoke POST for "+ uri );
                return (T) new JerseyHelper().setClient(client).post(uri.toString(), produce.toString(), headers, returnType,entity, cookie,scenarioId);
            }catch (Exception e){
                logger.error(JDRY,"fail invoke POST for uri "+ stepDescriptor.getChannel().getUri().getPath()+ " with args "+ Utils.result2String((Object[])args),e);
            }
            return null;
        }


    }

    public static class PUTMethodHandler implements ServiceCaller.UnaryClass  {

        @Override
        public <T> T invoke(IStepDescriptor stepDescriptor, Object[] args){
            try {
                stepDescriptor.setArgs(args);
                String scenarioId = stepDescriptor.getScenarioId();
                MediaType produce = stepDescriptor.getProduce();
                Client client = stepDescriptor.getClient();
                ChannelDescriptor channel = stepDescriptor.getChannel();
                MultivaluedMap<String, Object> headers = stepDescriptor.getHeaders();
                Entity<?> entity = stepDescriptor.getEntity();
                URI uri = channel.getUri();
                Cookie cookie = stepDescriptor.getCookie();
                Class returnType = stepDescriptor.getReturnType();
                logger.debug(JDRY,"invoke PUT for "+ uri + " with headers "+ headers.values() + " entity");
                return (T)new JerseyHelper().setClient(client).put(uri.toString(), produce.toString(), headers,  returnType,entity,cookie,scenarioId);
            }catch (Exception e){
                logger.error(JDRY,"fail invoke PUT for uri "+ stepDescriptor.getChannel().getUri().getPath()+ " with args "+ Utils.result2String((Object[])args),e);
            }
            return null;
        }
    }

    public static class DELETEMethodHandler implements ServiceCaller.UnaryClass  {

        @Override
        public <T> T invoke(IStepDescriptor stepDescriptor, Object[] args) {
            try {
                stepDescriptor.setArgs(args);
                String scenarioId = stepDescriptor.getScenarioId();
                MediaType produce = stepDescriptor.getProduce();
                Client client = stepDescriptor.getClient();
                ChannelDescriptor channel = stepDescriptor.getChannel();
                MultivaluedMap<String, Object> headers = stepDescriptor.getHeaders();
                Entity<?> entity = stepDescriptor.getEntity();
                URI uri = channel.getUri();
                Cookie cookie = stepDescriptor.getCookie();
                Class returnType = stepDescriptor.getReturnType();
                logger.debug(JDRY,"invoke DELETE for "+ uri  );
                return (T)new JerseyHelper().setClient(client).delete(uri.toString(), produce.toString(), headers,  returnType,cookie,scenarioId);
            }catch (Exception e){
                logger.error(JDRY,"fail invoke DELETE for uri "+ stepDescriptor.getChannel().getUri().getPath()+ " with args "+ Utils.result2String((Object[])args),e);
            }
            return null;
        }
    }

}
