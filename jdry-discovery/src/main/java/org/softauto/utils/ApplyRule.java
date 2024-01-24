package org.softauto.utils;


import org.softauto.config.Configuration;
import org.softauto.espl.Espl;

import java.util.ArrayList;

public class ApplyRule {

    private String rule;

    private Object rootObject;

    Object result = null;

    public <T> T getResult() {
        if(result != null){
            return (T)result;
        }
        return null;
    }

    public ApplyRule addContext(String key, Object value) {
        Espl.getInstance().addProperty(key,value);
        return this;
    }

    public ApplyRule setRootObject(Object rootObject) {
        this.rootObject = rootObject;
        return this;
    }

    private ApplyRule(String rule){
        this.rule = rule;
    }

    public static ApplyRule setRule(String rule) {
        return new ApplyRule(rule);
    }

    public ApplyRule apply(){
        if(Configuration.has(rule)){
            Object o= Configuration.get(rule).asObject();
            if(o instanceof ArrayList<?>){
                for(Object s : (ArrayList)o){
                    result = Espl.getInstance().addProperty("result",result).evaluate(s.toString());
                }
            }else {
                result =  Espl.getInstance().evaluate(o.toString());
            }
        }
        return this;
    }



}
