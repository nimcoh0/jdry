package org.softauto.jaxrs.service;

import org.softauto.core.Utils;
import org.softauto.jaxrs.SpringJwtHelper;

import javax.ws.rs.client.Entity;
import java.net.URI;
import java.util.HashMap;

public class SpringRestService {


    private static TestDefinition testDefinition = null;
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(RestService.class);

    public static TestDefinition getServiceDefinition(){
        return testDefinition;
    }

    public static <T> TestDefinition createTestDefinition(String stepName, Object[] args,Class[] types, HashMap<String,Object> callOptions) {
        return _createTestDefinition(stepName,args,types,callOptions);
    }

    public static <T> TestDefinition createTestDefinition(String stepName, Object[] args) {
        return _createTestDefinition(stepName,args,null,null);
    }

    private static <T> TestDefinition _createTestDefinition(String stepName, Object[] args,Class[] types,HashMap<String,Object> callOptions) {
        TestDefinition.Builder testDefinitionBuilder = null;
        try {
            TestDescriptor testDescriptor = TestDescriptor.create(stepName);
            testDefinitionBuilder = TestDefinition.builder(testDescriptor);
            //for(Item item : test.getItems()){
            //if (item.getProp("transceiver").equals("JAXRS")) {
            testDefinitionBuilder.addStep(testDescriptor.getStep(stepName,args,types,callOptions),ServiceCaller.call());
            //}
            //}
        }catch (Exception e){
            e.printStackTrace();
        }
        return testDefinition = testDefinitionBuilder.build();
    }

    public static class GETMethodHandler implements ServiceCaller.UnaryClass  {

        @Override
        public <T> T invoke(IStepDescriptor stepDescriptor, Object[] args) {
            return null;
        }
    }

    public static class POSTMethodHandler implements ServiceCaller.UnaryClass  {

        @Override
        public <T> T invoke(IStepDescriptor stepDescriptor, Object[] args) {
            try {
                stepDescriptor.setArgs(args);
               // MediaType produce = stepDescriptor.getProduce();
               // Client client = stepDescriptor.getClient();
                ChannelDescriptor channel = stepDescriptor.getChannel();
                //MultivaluedMap<String, Object> headers = stepDescriptor.getHeaders();

                Entity<?> entity = stepDescriptor.getEntity();
                URI uri = channel.getUri();
               // Cookie cookie = stepDescriptor.getCookie();
                Class returnType = stepDescriptor.getReturnType();
                //logger.debug("invoke PUT for "+ uri + " with headers "+ headers.values() + " entity");
                return (T)new SpringJwtHelper().post(uri.toString(),  entity, returnType,args);
            }catch (Exception e){
                logger.error("fail invoke PUT for uri "+ stepDescriptor.getChannel().getUri().getPath()+ " with args "+ Utils.result2String((Object[])args),e);
            }
            return null;
        }
    }

    public static class PUTMethodHandler implements ServiceCaller.UnaryClass  {

        @Override
        public <T> T invoke(IStepDescriptor stepDescriptor, Object[] args) {
            return null;
        }
    }

    public static class DELETEMethodHandler implements ServiceCaller.UnaryClass  {

        @Override
        public <T> T invoke(IStepDescriptor stepDescriptor, Object[] args) {
            return null;
        }
    }

}
