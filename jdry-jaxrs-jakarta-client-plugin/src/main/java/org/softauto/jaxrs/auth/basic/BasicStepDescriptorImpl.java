package org.softauto.jaxrs.auth.basic;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.softauto.core.Configuration;
import org.softauto.core.TestContext;
import org.softauto.jaxrs.AbstractStepDescriptorImpl;
import org.softauto.jaxrs.auth.EntityBuilder;
import org.softauto.jaxrs.service.ChannelDescriptor;
import org.softauto.jaxrs.util.ChannelBuilder;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


/**
 * impl Step Descriptor for basic auth
 */
public class BasicStepDescriptorImpl extends AbstractStepDescriptorImpl {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(BasicStepDescriptorImpl.class);

    @Override
    public Cookie getCookie(){
        Cookie cookie = null;
        try {
            if(TestContext.getScenario().getProperty("JSESSIONID") != null){
                cookie = (Cookie) TestContext.getScenario().getProperty("JSESSIONID");
            }
            logger.debug("successfully build cookie ");
        } catch (Exception e) {
            logger.error("fail build cookie",e);
        }
        return cookie;
    }

    @Override
    public Client getClient() {
        Client client = null;
        try {
            ClientConfig clientConfig = new ClientConfig();
            clientConfig.register(JacksonFeature.class);
            clientConfig.register(ResponseClientFilter.class);
            clientConfig.register(MultiPartFeature.class);
            client =  jakarta.ws.rs.client.ClientBuilder.newBuilder().withConfig(clientConfig).build();
            logger.debug("successfully build client ");
        }catch (Exception e){
            logger.error("fail build client ",e);
        }
        return client;
    }



    @Override
    public ChannelDescriptor getChannel() {
        ChannelDescriptor channelDescriptor = null;
        try {
            String host = (Configuration.get("jaxrs").asMap().get("host").toString());
            String port = (Configuration.get("jaxrs").asMap().get("port").toString());
            String protocol = (Configuration.get("jaxrs").asMap().get("protocol").toString());
            String baseUrl = Configuration.get("jaxrs").asMap().containsKey("base_url") ? (Configuration.get("jaxrs").asMap().get("base_url").toString()):"";
            String newPath = callOptions.get("path").toString();
            if(!newPath.contains(baseUrl)){
                if(newPath.startsWith("/")){
                    newPath = baseUrl+newPath;
                }else {
                    newPath = baseUrl+"/"+newPath;
                }
            }
            channelDescriptor = ChannelBuilder.newBuilder().setHost(host)
                    .setProtocol(protocol)
                    .setArgs(args)
                    .setPath(newPath)
                    .setPort(port)
                    .build()
                    .getChannelDescriptor();
            logger.debug("successfully build channel ");
        } catch (Exception e) {
            logger.error("fail build channel ",e);
        }
        return channelDescriptor;
    }




    @Override
    public MultivaluedMap<String, Object> getHeaders() {
        MultivaluedMap<String, Object> mm = new MultivaluedHashMap<>();
        try {
            if(TestContext.getScenario() != null ) {
                mm.add("scenarioId", scenarioId);
                Object jsessioId = TestContext.getScenario().getProperty("JSESSIONID");
                if(jsessioId == null){
                    Object[] args =  (Object[])TestContext.getScenario().getProperty("args");
                    if(args != null && args.length ==2) {
                        String valueToEncode = args[0] + ":" + args[1];
                        String pass = "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
                        mm.add("Authorization", pass);
                    }
                }
            }
            if(callOptions.get("headers") != null){
                mm.putAll((MultivaluedMap<String, Object>)callOptions.get("headers"));
            }
            logger.debug("successfully build headers ");
        } catch (Exception e) {
            logger.error("fail build headers ",e);
        }
        return mm;
    }

    @Override
    public Entity<?> getEntity() {
        Entity<?> entity = null;
        try {
            List<String> argumentsNames = (ArrayList)callOptions.get("argumentsNames");
            entity = (Entity<?>) EntityBuilder.newBuilder().setCallOptions(callOptions).setProduce(consume).setArgs(args).setArgsNames(argumentsNames).build().getEntity();
            logger.debug("successfully build entity ");
        } catch (Exception e) {
            logger.error("fail build entity",e);
        }
        return entity;
    }




}
