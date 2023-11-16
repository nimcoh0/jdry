package org.softauto.jaxrs.util;

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
        return threadlocal.get();
    }

    public Object get(String name) {
        HashMap<String, Object> map =  threadlocal.get();
        if(map != null && map.size() > 0 && map.containsKey(name)) {
            return map.get(name);
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
