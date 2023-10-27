package org.softauto.analyzer.core.utils;

import org.softauto.analyzer.core.system.config.Configuration;

public class getConfigurationParameter {

    Object result;

    public getConfigurationParameter(String param){
        if(Configuration.has(param)){
            result = Configuration.get(param).asObject();
        }
    }

    public static getConfigurationParameter param(String param){
        return new getConfigurationParameter(param);
    }

    public Boolean asBoolen(){
        return (Boolean)result;
    }

    public String asString(){
        return result.toString();
    }



}
