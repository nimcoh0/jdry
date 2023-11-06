package org.softauto.auth.basic;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.softauto.EntityBuilder;
import org.softauto.core.Configuration;
import org.softauto.core.TestContext;
import org.softauto.jaxrs.AbstractStepDescriptorImpl;
import org.softauto.jaxrs.service.ChannelDescriptor;
import org.softauto.jaxrs.util.ChannelBuilder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class BasicStepDescriptorImpl extends AbstractStepDescriptorImpl {


    @Override
    public Cookie getCookie(){
        if(TestContext.getScenario().getProperty("JSESSIONID") != null){
          return (Cookie) TestContext.getScenario().getProperty("JSESSIONID");
        }
        return null;
    }

    @Override
    public Client getClient() {
        Client client = null;
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(JacksonFeature.class);
        clientConfig.register(ResponseClientFilter.class);
        //clientConfig.register(ClientHttpRequestInterceptor.class);
        clientConfig.register(MultiPartFeature.class);
        client = jakarta.ws.rs.client.ClientBuilder.newBuilder().withConfig(clientConfig).build();
        return client;
    }

    public ChannelDescriptor getChannel() {
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
        return ChannelBuilder.newBuilder().setHost(host)
                .setProtocol(protocol)
                .setArgs(args)
                .setPath(newPath)
                .setPort(port)
                .build()
                .getChannelDescriptor();
    }

    @Override
    public MultivaluedMap<String, Object> getHeaders() {
        MultivaluedMap<String, Object> mm = new MultivaluedHashMap<>();

        if(TestContext.getScenario() != null ) {
            mm.add("scenarioId", scenarioId);
            Object jsessioId = TestContext.getScenario().getProperty("JSESSIONID");
            if(jsessioId == null){
                Object[] args =  (Object[])TestContext.getScenario().getProperty("args");
                //Object[] args = (Object[]) Threadlocal.getInstance().get("args");
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

        return mm;
    }

    @Override
    public Entity<?> getEntity() {
        List<String> argumentsNames = (ArrayList)callOptions.get("argumentsNames");
        return EntityBuilder.newBuilder().setCallOptions(callOptions).setProduce(consume).setArgs(args).setArgsNames(argumentsNames).build().getEntity();
    }
}
