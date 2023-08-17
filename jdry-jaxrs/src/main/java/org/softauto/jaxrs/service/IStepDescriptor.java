package org.softauto.jaxrs.service;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
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

    Class getReturnType();

    String getScenarioId();

    IStepDescriptor build();

    HashMap<String, Object> getCallOptions();

   // void  setConfiguration(HashMap<String, Object> configuration);

    void setArgs(Object[] args);

    void setTypes(Class[] types);

    //void  setMethod (ServiceCaller.UnaryClass method)  ;

    ServiceCaller.UnaryClass getMethodImpl();

    Cookie getCookie();

    void setCallOptions(HashMap<String,Object> callOptions);

    void saveAuth(jakarta.ws.rs.core.Response res);

}
