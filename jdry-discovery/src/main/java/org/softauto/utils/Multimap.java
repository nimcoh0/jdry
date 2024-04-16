package org.softauto.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Multimap<T,K> {

    HashMap<T, K> map = new HashMap<>();

    public HashMap<T, K> getMap() {
        return map;
    }

    public int size(){
        return map.size();
    }

    public void putAll(HashMap<T, K> hm ){
        for(Map.Entry entry :hm.entrySet()){
            put((T) entry.getKey().toString(), (K) entry.getValue());
        }
    }

    public void replace(Multimap<T, K> multimap){
        HashMap<T, K> _map = multimap.getMap();
        map.putAll(_map);
    }

    public void putAll(Multimap<T, K> multimap){
        HashMap<T, K> _map = multimap.getMap();
        for(Map.Entry entry :_map.entrySet()){
            put((T) entry.getKey().toString(), (K) entry.getValue());
        }
    }

    public void put(T key, K value){
        put(key,value,false);
    }

    public void put(T key, K value,boolean unique){
       if(map.containsKey(key) ){
           Object o =  map.get(key);
           if(o instanceof ArrayList){
               ((List<Object>)o).add(value);
           }else if(value instanceof Multimap){
               List<Object> list = new ArrayList<>();
               HashMap<T, K> hm = new HashMap<>();
               for(Map.Entry entry : ((Multimap<T,K>) value).getMap().entrySet()){
                   hm.put((T) entry.getKey().toString(), (K) entry.getValue());
               }
               //hm.putAll((HashMap<String, Object>) o);
               list.add(hm);
               list.add(o);
               map.put((T) key, (K) list);
            }else {
                List<Object> list = new ArrayList<>();
                list.add(o);
                if(unique ){
                    if(!list.contains(value))
                         list.add(value);
                }else {
                    list.add(value);
                }
                map.put((T) key, (K) list);
            }
        }else {

            if(value instanceof Multimap){
                //List<Object> list = new ArrayList<>();
                HashMap<T, K> hm = new HashMap<>();
                for(Map.Entry entry : ((Multimap<T,K>) value).getMap().entrySet()){
                    hm.put((T) entry.getKey().toString(), (K) entry.getValue());
                }
                //list.add(hm);
                map.put((T) key, (K) hm);
            }else {
               map.put((T) key, (K) value);
            }
        }
    }


}
