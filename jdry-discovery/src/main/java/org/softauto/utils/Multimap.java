package org.softauto.utils;

import java.util.*;

public class Multimap {

    LinkedHashMap<String, Object> map = new LinkedHashMap();

    public LinkedHashMap<String, Object> getMap() {
        return map;
    }

    public int size(){
        return map.size();
    }

    public void putAll(LinkedHashMap<String, Object> hm ){
        for(Map.Entry entry :hm.entrySet()){
            put(entry.getKey().toString(),entry.getValue(),false);
        }
    }

    public void replace(Multimap multimap){
        LinkedHashMap<String, Object> _map = multimap.getMap();
        map.putAll(_map);
    }

    public void putAll(Multimap multimap){
        LinkedHashMap<String, Object> _map = multimap.getMap();
        for(Map.Entry entry :_map.entrySet()){
            put(entry.getKey().toString(),entry.getValue(),false);
        }
    }

    public void put(String key, Object value){
        put(key, value,false);
    }


    public void put(String key, Object value,boolean replace){

        if(!replace && map.containsKey(key)){
           Object o =  map.get(key);
           if(o instanceof ArrayList) {
               if (value instanceof String && !((List<Object>) o).contains(value)) {
                   ((List<Object>) o).add(value);
               } else if (value instanceof Multimap) {
                   ((List<Object>) o).add(((Multimap) value).getMap());
               } else {
                   System.out.println("Multimap didn't put " + value.getClass().getTypeName());
               }
           }else if(o instanceof LinkedList<?>){
                   if(value instanceof String && !((LinkedList<Object>)o).contains(value)){
                       ((LinkedList<Object>)o).add(value);
                   }else if(value instanceof Multimap){
                       ((LinkedList<Object>) o).add(((Multimap) value).getMap());
                   }else {
                       System.out.println("Multimap didn't put " + value.getClass().getTypeName());
                   }
           }else if(value instanceof Multimap){
               LinkedList<Object> list = new LinkedList();
               LinkedHashMap<String, Object> hm = new LinkedHashMap();
               list.add(o);
               for(Map.Entry entry : ((Multimap) value).getMap().entrySet()){
                   hm.put(entry.getKey().toString(),  entry.getValue());
               }

               list.add(hm);
               //list.add(o);
               map.put(key, list);
            }else {
               LinkedList<Object> list = new LinkedList();
               list.add(o);
               list.add(value);
               map.put(key, list);
            }
        }else {

            if(value instanceof Multimap){
                LinkedHashMap<String, Object> hm = new LinkedHashMap();
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
