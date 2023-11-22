package org.softauto.analyzer.model.scenario;



import java.io.Serializable;
import java.util.*;

public class Scenario implements Cloneable, Serializable {

    /**
     * list of tests names that include in this scenario
     */
    LinkedList<String> tests = new LinkedList<>();

    Integer order;

    /**
     * scenario UUID
     */
    String id ;

    String namespace;

    /**
     * scenario name
     */
    String suiteName;

    /**
     * scenario state (can be run,pass,fail,skip)
     */
    String state;

    /**
     * list of errors
     */
    List<Object> error = new ArrayList<>();

    /**
     * scenario configuration
     */
    HashMap<String,Object> configuration = new HashMap<>();

    HashMap<String,Object> properties = new HashMap<>();

    public HashMap<String, Object> getProperties() {
        return properties;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Object> getError() {
        return error;
    }

    public void setError(List<Object> error) {
        this.error = error;
    }

    public void addError(Object error) {
        this.error.add(error);
    }

    public Object getProperty(String key) {
        if(properties.containsKey(key)){
           return properties.get(key);
        }
        return null;
    }


    public HashMap<String, Object> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(HashMap<String, Object> configuration) {
        this.configuration = configuration;
    }

    public void setProperties(HashMap<String, Object> properties) {
        this.properties = properties;
    }

    public void addProperty(String key,Object value) {
        this.properties.put(key,value);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LinkedList<String> getTests() {
        return tests;
    }

    public Scenario setTests(LinkedList<String> tests) {
        this.tests = tests;
        return this;
    }

    public Integer getOrder() {
        return order;
    }

    public Scenario setOrder(Integer order) {
        this.order = order;
        return this;
    }

    public String getId() {
        return id;
    }



    public String getNamespace() {
        return namespace;
    }

    public Scenario setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public String getSuiteName() {
        return suiteName;
    }

    public Scenario setSuiteName(String suiteName) {
        this.suiteName = suiteName;
        return this;
    }

    public Scenario addTest(String fullName) {
        this.tests.add(fullName);
        return this;
    }



    public boolean isExist(String name){
        for(String s : tests){
            if(s.equals(name)){
                return true;
            }
        }
        return false;
    }
}
