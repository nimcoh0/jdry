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

    public static String getScenarioKey(){
           return   scenarios.keySet().toArray(new String[1])[0].toString();
    }

    public static Scenario getScenario(String id){
        if(id == null){
          String key =   scenarios.keySet().toArray(new String[1])[0].toString();
          return scenarios.values().toArray(new Scenario[1])[0];
        }
        return scenarios.get(id);
    }

    public static void addScenario(String id){
        Scenario scenario = new Scenario();
        scenarios.put(id,scenario);
    }

}
