package org.softauto.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestContext {

    static HashMap<String,Object> hm = new HashMap<>();

    static HashMap<String,Scenario> scenarios = new HashMap<>();

    public static Object get(String name){
        return hm.get(name);
    }

    public static void put(String key,Object value){
        hm.put(key,value);
    }

    public static void remove(String key){
        hm.remove(key);
    }

    public static boolean has(String name){
        if(hm.containsKey(name)){
            return true;
        }
        return false;
    }

    public static Scenario getScenario(String id){
        return scenarios.get(id);
    }

    public static void addScenario(String id){
        Scenario scenario = new Scenario();
        scenarios.put(id,scenario);
    }

}
