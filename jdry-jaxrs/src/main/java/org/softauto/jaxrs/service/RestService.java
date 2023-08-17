package org.softauto.jaxrs.service;



import org.softauto.core.Utils;
import org.softauto.jaxrs.JerseyHelper;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import java.net.URI;
import java.util.HashMap;


public class RestService {



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
            try {
                //HashMap<String,Object> callOptions = (HashMap<String, Object>) args[2];
                stepDescriptor.setArgs(args);
                MediaType produce = stepDescriptor.getProduce();
                Client client = stepDescriptor.getClient();
                ChannelDescriptor channel = stepDescriptor.getChannel();
                MultivaluedMap<String, Object> headers = stepDescriptor.getHeaders();

                URI uri = channel.getUri();
                Cookie cookie = stepDescriptor.getCookie();
                Class returnType = stepDescriptor.getReturnType();
                //return (T) new JerseyHelper(client).get(uri.toString(), produce.toString(), headers, stepDescriptor.getReturnType());

/*
                        ClientBuilder.newBuilder().setPassword(stepDescriptor.getAuthenticationPassword())
                        .setUsername(stepDescriptor.getAuthenticationUserName())
                        .setSchema(stepDescriptor.getAuthenticationSchema())
                        .build()
                        .getClient();
                ChannelDescriptor channel = ChannelBuilder.newBuilder().setHost(stepDescriptor.getHost())
                        .setProtocol(stepDescriptor.getProtocol())
                        .setArgs(args)
                        .setPath(stepDescriptor.getPath())
                        .setPort(stepDescriptor.getPort())
                        .setBaseUrl(stepDescriptor.getBase_url())
                        .build()
                        .getChannelDescriptor();
                MultivaluedMap<String, Object> headers = HeaderBuilder.newBuilder().build().getHeaders();
                MediaType produce = stepDescriptor.getProduce();

                URI uri =  channel.getUri();


 */
                logger.debug("invoke GET for "+ uri);
                return (T) new JerseyHelper().setClient(client).get(uri.toString(), produce.toString(), headers, returnType,cookie);
            }catch (Exception e){
                logger.error("fail invoke GET for uri "+ stepDescriptor.getChannel().getUri().getPath()+ " with args "+ Utils.result2String((Object[])args),e);
            }
            return null;
        }
    }

    public static class POSTMethodHandler implements ServiceCaller.UnaryClass  {



        @Override
        public <T> T invoke(IStepDescriptor stepDescriptor, Object[] args) {
            try {
                stepDescriptor.setArgs(args);
                MediaType produce = stepDescriptor.getConsume();
                Client client = stepDescriptor.getClient();
                ChannelDescriptor channel = stepDescriptor.getChannel();
                MultivaluedMap<String, Object> headers = stepDescriptor.getHeaders();
                Entity<?> entity = stepDescriptor.getEntity();
                URI uri = channel.getUri();
                Cookie cookie = stepDescriptor.getCookie();
                Class returnType = stepDescriptor.getReturnType();
                String scenarioId = stepDescriptor.getScenarioId();
                /*
                Client client = ClientBuilder.newBuilder().setPassword(stepDescriptor.getAuthenticationPassword())
                        .setUsername(stepDescriptor.getAuthenticationUserName())
                        .setSchema(stepDescriptor.getAuthenticationSchema())
                        .build()
                        .getClient();
                ChannelDescriptor channel = ChannelBuilder.newBuilder().setHost(stepDescriptor.getHost())
                        .setProtocol(stepDescriptor.getProtocol())
                        .setArgs(args)
                        .setPath(stepDescriptor.getPath())
                        .setPort(stepDescriptor.getPort())
                        .setBaseUrl(stepDescriptor.getBase_url())
                        .build()
                        .getChannelDescriptor();
                MultivaluedMap<String, Object> headers = HeaderBuilder.newBuilder().build().getHeaders();
                MediaType produce = stepDescriptor.getProduce();
                Entity<?> entity = EntityBuilder.newBuilder().setProduce(produce).setArgs(args).setArgsNames(stepDescriptor.getArgsNames()).build().getEntity();

                URI uri =  channel.getUri();

                 */
                //Entity<?> entity = org.softauto.jaxrs.Utils.buildEntity(produces,(Object[])args[0]);
                logger.debug("invoke POST for "+ uri );

                //post(String url, String mediaType, MultivaluedMap<String, Object> headers, Class<T> response, Entity<?> entity,Cookie cookie
                return (T) new JerseyHelper().setClient(client).post(uri.toString(), produce.toString(), headers, returnType,entity, cookie,scenarioId);
                //HashMap<String, Object> callOption = stepDescriptor.getCallOptions();
                //if (callOption.get("role") != null && callOption.get("role").toString().equals("AUTH")) {
                   //stepDescriptor.saveAuth(res);
                //}
                //return res;
            }catch (Exception e){
                logger.error("fail invoke POST for uri "+ stepDescriptor.getChannel().getUri().getPath()+ " with args "+ Utils.result2String((Object[])args),e);
            }
            return null;
        }


    }

    public static class PUTMethodHandler implements ServiceCaller.UnaryClass  {

        @Override
        public <T> T invoke(IStepDescriptor stepDescriptor, Object[] args){
            try {
                stepDescriptor.setArgs(args);
                MediaType produce = stepDescriptor.getProduce();
                Client client = stepDescriptor.getClient();
                ChannelDescriptor channel = stepDescriptor.getChannel();
                MultivaluedMap<String, Object> headers = stepDescriptor.getHeaders();

                Entity<?> entity = stepDescriptor.getEntity();
                URI uri = channel.getUri();
                Cookie cookie = stepDescriptor.getCookie();
                Class returnType = stepDescriptor.getReturnType();
                /*
                Client client = ClientBuilder.newBuilder().setPassword(stepDescriptor.getAuthenticationPassword())
                        .setUsername(stepDescriptor.getAuthenticationUserName())
                        .setSchema(stepDescriptor.getAuthenticationSchema())
                        .build()
                        .getClient();
                ChannelDescriptor channel = ChannelBuilder.newBuilder().setHost(stepDescriptor.getHost())
                        .setProtocol(stepDescriptor.getProtocol())
                        .setArgs(args)
                        .setPath(stepDescriptor.getPath())
                        .setPort(stepDescriptor.getPort())
                        .setBaseUrl(stepDescriptor.getBase_url())
                        .build()
                        .getChannelDescriptor();
                MultivaluedMap<String, Object> headers = HeaderBuilder.newBuilder().build().getHeaders();
                MediaType produce = stepDescriptor.getProduce();
                Entity<?> entity = EntityBuilder.newBuilder().setProduce(produce).setArgs(args).setArgsNames(stepDescriptor.getArgsNames()).build().getEntity();


                 */
                //URI uri =  channel.getUri();
                //Entity<?> entity = org.softauto.jaxrs.Utils.buildEntity(produces,(Object[])args[0]);
                logger.debug("invoke PUT for "+ uri + " with headers "+ headers.values() + " entity");
                return (T)new JerseyHelper().setClient(client).put(uri.toString(), produce.toString(), headers,  returnType,entity,cookie);
            }catch (Exception e){
                logger.error("fail invoke PUT for uri "+ stepDescriptor.getChannel().getUri().getPath()+ " with args "+ Utils.result2String((Object[])args),e);
            }
            return null;
        }
    }

    public static class DELETEMethodHandler implements ServiceCaller.UnaryClass  {

        @Override
        public <T> T invoke(IStepDescriptor stepDescriptor, Object[] args) {
            try {
                stepDescriptor.setArgs(args);
                MediaType produce = stepDescriptor.getProduce();
                Client client = stepDescriptor.getClient();
                ChannelDescriptor channel = stepDescriptor.getChannel();
                MultivaluedMap<String, Object> headers = stepDescriptor.getHeaders();

                Entity<?> entity = stepDescriptor.getEntity();
                URI uri = channel.getUri();
                Cookie cookie = stepDescriptor.getCookie();
                Class returnType = stepDescriptor.getReturnType();
                /*
                Client client = ClientBuilder.newBuilder().setPassword(stepDescriptor.getAuthenticationPassword())
                        .setUsername(stepDescriptor.getAuthenticationUserName())
                        .setSchema(stepDescriptor.getAuthenticationSchema())
                        .build()
                        .getClient();
                ChannelDescriptor channel = ChannelBuilder.newBuilder().setHost(stepDescriptor.getHost())
                        .setProtocol(stepDescriptor.getProtocol())
                        .setArgs(args)
                        .setPath(stepDescriptor.getPath())
                        .setPort(stepDescriptor.getPort())
                        .setBaseUrl(stepDescriptor.getBase_url())
                        .build()
                        .getChannelDescriptor();
                MultivaluedMap<String, Object> headers = HeaderBuilder.newBuilder().build().getHeaders();
                MediaType produce = stepDescriptor.getProduce();
                Entity<?> entity = EntityBuilder.newBuilder().setProduce(produce).setArgs(args).setArgsNames(stepDescriptor.getArgsNames()).build().getEntity();

                URI uri =  channel.getUri();

                 */
                logger.debug("invoke DELETE for "+ uri  );
                return (T)new JerseyHelper().setClient(client).delete(uri.toString(), produce.toString(), headers,  returnType,cookie);
            }catch (Exception e){
                logger.error("fail invoke DELETE for uri "+ stepDescriptor.getChannel().getUri().getPath()+ " with args "+ Utils.result2String((Object[])args),e);
            }
            return null;
        }
    }

}
