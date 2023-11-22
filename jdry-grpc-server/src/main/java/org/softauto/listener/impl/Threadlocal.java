package org.softauto.listener.impl;

import java.util.HashMap;

public class Threadlocal {

    ThreadLocal<HashMap<String, Object>> threadlocal = new ThreadLocal<>();

    private static Threadlocal jaxRsIdHolder;

    public static Threadlocal getInstance(){
        if(jaxRsIdHolder == null){
            jaxRsIdHolder = new Threadlocal();
        }
        return jaxRsIdHolder;
    }

    public HashMap<String, Object>get() {
        if(threadlocal.get() != null) {
            return threadlocal.get();
        }
        return null;
    }

    public boolean has(String key){
        if(threadlocal.get() != null) {
            if (threadlocal.get().containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    public String get(String name) {
        HashMap<String, Object> map =  threadlocal.get();
        if(map != null && map.size() > 0 && map.containsKey(name)) {
            return map.get(name).toString();
        }
        return null;
    }

    public void set(HashMap<String, Object>map) {
        threadlocal.set(map);
    }

    public void add(String key,Object value) {
        HashMap<String,Object> hm = new HashMap<>();
        hm.put(key,value);
        threadlocal.set(hm);
    }

}
