package org.softauto.jaxrs.service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.HashMap;
import java.util.Map;

public interface IStepDescriptor {

    ChannelDescriptor getChannel() ;

    Client getClient();

    MultivaluedMap<String, Object> getHeaders();

    Entity<?> getEntity();

    MediaType getProduce();

    MediaType getConsume();

    Map<String, Object> getProperties();

    void setFullMethodName(String fullMethodName);

    String getFullMethodName();



    boolean isSession();

    IStepDescriptor build();

    void  setConfiguration(HashMap<String, Object> configuration);

    void setArgs(Object[] args);

    void  setMethod (ServiceCaller.UnaryClass method)  ;

    ServiceCaller.UnaryClass getMethod();

    Cookie getCookie();

    void setCallOptions(HashMap<String,Object> callOptions);



}
