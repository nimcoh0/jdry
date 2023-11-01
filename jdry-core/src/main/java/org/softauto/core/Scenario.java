package org.softauto.core;

import java.util.HashMap;

public class Scenario {

    String id ;

    HashMap<String,Object> properties = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<String, Object> properties) {
        this.properties = properties;
    }

    public void addProperty(String key,Object value) {
        this.properties.put(key,value);
    }

    public Object getProperty(String key){
        return this.properties.get(key);
    }
}
