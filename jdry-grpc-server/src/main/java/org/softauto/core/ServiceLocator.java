package org.softauto.core;

import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {

    private static ServiceLocator serviceLocator;

    Map<String, Object> services;

    public static ServiceLocator getInstance(){
        if(serviceLocator == null){
            serviceLocator =  new ServiceLocator();
        }
        return serviceLocator;
    }

    private ServiceLocator(){
        services = new HashMap<>();
    }

    public void register(String name, Object service){
        services.put(name,service);
    }

    public Object getService(String name){
        return services.get(name);
    }

}
