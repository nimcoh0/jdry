package org.softauto.core;

import java.util.HashMap;
import java.util.Map;

public interface IStepDescriptor {

    String getFullMethodName();

    void setFullMethodName(String fullMethodName);

    IChannelDescriptor getChannel() ;

    HashMap<String, Object> getProperties();

    void setProperties(Map<String, Object> map);


    Class getReturnType();

    String getScenarioId();

    void setScenarioId(String scenarioId);

    IStepDescriptor build();

    void setArgs(Object[] args);

    Object[] getArgs();

    void setTypes(Class[] types);

    Class[] getTypes();

    ServiceCaller.UnaryClass getMethodImpl();

}
