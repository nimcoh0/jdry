package org.softauto.jaxrs.service;

import org.softauto.core.Configuration;
import org.softauto.core.TestContext;
import org.softauto.jaxrs.annotations.AuthenticationType;
import org.softauto.jaxrs.util.ChannelBuilder;
import org.softauto.jaxrs.util.ClientBuilder;
import org.softauto.jaxrs.util.EntityBuilder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import java.util.*;

public class DefaultStepDescriptorImpl implements IStepDescriptor{

    HashMap<String,Object> callOptions;

    Object[] args;

    Class[] types;

    MediaType produce;

    MediaType consume;

    String fullMethodName;

    String httpMethod;

    public void setCallOptions(HashMap<String,Object> callOptions) {
        this.callOptions = callOptions;
    }

    public Cookie getCookie(){
        return (Cookie) TestContext.get("sessionId");
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public void setTypes(Class[] types) {
        this.types = types;
    }



    public Map<String, Object> getProperties() {
        return callOptions.get("props") != null ? (Map<String, Object>) callOptions.get("props") : new HashMap<>();
    }

    @Override
    public void setFullMethodName(String fullMethodName) {
        this.fullMethodName = fullMethodName;
    }

    @Override
    public String getFullMethodName() {
        return fullMethodName;
    }

    public ChannelDescriptor getChannel() {
        String host = (Configuration.get("jaxrs").asMap().get("host").toString());
        String port = (Configuration.get("jaxrs").asMap().get("port").toString());
        String protocol = (Configuration.get("jaxrs").asMap().get("protocol").toString());
        //String base_url = ( Configuration.get("jaxrs").asMap().get("base_url").toString());
        return ChannelBuilder.newBuilder().setHost(host)
                .setProtocol(protocol)
                .setArgs(args)
                .setPath(callOptions.get("path").toString())
                .setPort(port)
                //.setBaseUrl(base_url)
                .build()
                .getChannelDescriptor();
    }

    public Client getClient() {
        Client client = null;
        String auth = Configuration.get("jaxrs").asMap().get(org.softauto.jaxrs.configuration.Context.AUTH).toString();
        if(auth != null && auth.equals(AuthenticationType.BASIC.getValue()) && TestContext.get("sessionId") == null && callOptions.get("role").toString().equals("AUTH") ) {
            client =  ClientBuilder.newBuilder().setPassword(args[1].toString())
                    .setUsername(args[0].toString())
                    .build()
                    .getClient();
        }else {
            client = ClientBuilder.newBuilder().build().getClient();
        }
        Map<String, Object> map = getProperties();
        if(map != null && map.size() > 0) {
            for (Map.Entry entry : map.entrySet()) {
                client.property(entry.getKey().toString(), entry.getValue());
            }
        }
        return client;
    }

    public MultivaluedMap<String, Object> getHeaders() {
        return callOptions.get("headers") != null ? (MultivaluedMap<String, Object>) callOptions.get("headers") : new MultivaluedHashMap<>();
    }

    public Class getReturnType(){
        try {
            String s = callOptions.get("response").toString();
            Class c = Class.forName(s);
            return c;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IStepDescriptor build() {
        return this;
    }

    public Entity<?> getEntity() {
        List<String> argumentsNames = (ArrayList)callOptions.get("argumentsNames");
        return EntityBuilder.newBuilder().setProduce(consume).setArgs(args).setArgsNames(argumentsNames).build().getEntity();
    }


    public MediaType getProduce() {
        return produce = MediaType.valueOf(callOptions.get("produces").toString());
    }


    public MediaType getConsume() {
        return consume = MediaType.valueOf(callOptions.get("consumes").toString());
    }

    public void getMethod(){
        httpMethod = callOptions.get("method").toString();
    }

    public ServiceCaller.UnaryClass getMethodImpl(){
        ServiceCaller.UnaryClass method = null ;
        getMethod();
        if(httpMethod.equals("POST")){
            method = new RestService.POSTMethodHandler();
        }
        if(httpMethod.equals("GET")){
            method = new RestService.GETMethodHandler();
        }
        if(httpMethod.equals("PUT")){
            method = new RestService.PUTMethodHandler();
        }
        if(httpMethod.equals("DELETE")){
            method = new RestService.DELETEMethodHandler();
        }

        return method;
    }
}
