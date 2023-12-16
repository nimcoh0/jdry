package org.softauto.analyzer.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Multimap {

    HashMap<String, Object> map = new HashMap<>();

    public HashMap<String, Object> getMap() {
        return map;
    }

    public int size(){
        return map.size();
    }

    public void putAll(HashMap<String, Object> hm ){
        for(Map.Entry entry :hm.entrySet()){
            put(entry.getKey().toString(),entry.getValue());
        }
    }

    public void replace(Multimap multimap){
        HashMap<String, Object> _map = multimap.getMap();
        map.putAll(_map);
    }

    public void putAll(Multimap multimap){
        HashMap<String, Object> _map = multimap.getMap();
        for(Map.Entry entry :_map.entrySet()){
            put(entry.getKey().toString(),entry.getValue());
        }
    }

    public void put(String key, Object value){
        if(map.containsKey(key)){
           Object o =  map.get(key);
           if(o instanceof ArrayList){
               ((List<Object>)o).add(value);
           }else if(value instanceof Multimap){
               List<Object> list = new ArrayList<>();
               HashMap<String, Object> hm = new HashMap<>();
               for(Map.Entry entry : ((Multimap) value).getMap().entrySet()){
                   hm.put(entry.getKey().toString(),  entry.getValue());
               }

               list.add(hm);
               list.add(o);
               map.put(key, list);
            }else {
                List<Object> list = new ArrayList<>();
                list.add(value);
                list.add(o);
                map.put(key, list);
            }
        }else {

            if(value instanceof Multimap){

                HashMap<String, Object> hm = new HashMap<>();
                for(Map.Entry entry : ((Multimap) value).getMap().entrySet()){
                    hm.put(entry.getKey().toString(),  entry.getValue());
                }

                map.put(key, hm);
            }else {
               map.put(key, value);
            }
        }
    }


}
