package org.softauto.core;

import java.util.HashMap;

public class TestContext {

    static HashMap<String,Object> hm = new HashMap<>();



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


}
