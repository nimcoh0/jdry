package org.softauto.jaxrs.auth.none;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
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
import java.util.List;

public class NoneStepDescriptorImpl extends AbstractStepDescriptorImpl {


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
        clientConfig.register(MultiPartFeature.class);
        client = javax.ws.rs.client.ClientBuilder.newBuilder().withConfig((javax.ws.rs.core.Configuration) clientConfig).build();
        return client;
    }



    public MultivaluedMap<String, Object> getHeaders() {
        MultivaluedMap<String, Object> mm = new MultivaluedHashMap<>();

        if(TestContext.getScenario() != null ) {
            mm.add("scenarioId", scenarioId);
        }

        if(callOptions != null && callOptions.containsKey("headers") && callOptions.get("headers") != null){
            mm.putAll((MultivaluedMap<String, Object>)callOptions.get("headers"));
        }

        return mm;
    }


}
