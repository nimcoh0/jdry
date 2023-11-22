package org.softauto.jaxrs.service;

import jakarta.ws.rs.core.MediaType;

import java.util.HashMap;
import java.util.Map;

public interface IStepDescriptor {

    ChannelDescriptor getChannel() ;

    <T> T getClient();

    <T> T getHeaders();

    <T> T getEntity();

    MediaType getProduce();

    MediaType getConsume();

    Map<String, Object> getProperties();

    void setFullMethodName(String fullMethodName);

    String getFullMethodName();

    Class getReturnType();

    String getScenarioId();

    void setScenarioId(String scenarioId);

    IStepDescriptor build();

    HashMap<String, Object> getCallOptions();

    void setArgs(Object[] args);

    void setTypes(Class[] types);

    ServiceCaller.UnaryClass getMethodImpl();

    <T> T getCookie();

    void setCallOptions(HashMap<String,Object> callOptions);



}
