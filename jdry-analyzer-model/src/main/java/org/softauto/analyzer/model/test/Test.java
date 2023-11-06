package org.softauto.analyzer.model.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.ClassUtils;
import org.softauto.analyzer.model.Item;
import org.softauto.analyzer.model.api.Api;
import org.softauto.analyzer.model.asserts.Assert;
import org.softauto.analyzer.model.data.Data;
import org.softauto.analyzer.model.expected.Expected;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test implements Item {

    //private String result;

    protected Assert anAssert;

    private String name;

    private String namespace;

    private String testId;

    private Data data;

    private Api api;

    private int order;

    private HashMap<String,Object> publish = new HashMap<>();

    private String fullName;

    //private String resultType;

    private String resultPublishName;

    private Expected expected;

    private String context;

    private String subject;

    private TestState state;

    private List<HashMap<String, String>> crud = new ArrayList<>();

    public List<HashMap<String, String>> getCrud() {
        return crud;
    }

    public void setCrud(List<HashMap<String, String>> crud) {
        this.crud = crud;
    }

    public void addCrud(String key,String value) {
        HashMap<String,String> hm = new HashMap<>();
        hm.put(key,value);
        this.crud.add(hm);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Expected getExpected() {
        return expected;
    }

    public void setExpected(Expected expected) {
        this.expected = expected;
    }

    public String getResultPublishName() {
        return resultPublishName;
    }

    public void setResultPublishName(String resultPublishName) {
        this.resultPublishName = resultPublishName;
    }

    /*
    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getResult() {
        return result;
    }



    public Test setResult(String result) {
        this.result = result;
        return this;
    }
*/
    public Assert getAnAssert() {
        return anAssert;
    }

    public Test setAnAssert(Assert anAssert) {
        this.anAssert = anAssert;
        return this;
    }



    public String getName() {
        return name;
    }

    public Test setName(String name) {
        this.name = name;
        return this;
    }

    public String getNamespace() {
        return namespace;
    }

    public Test setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public String getTestId() {
        return testId;
    }

    public Test setTestId(String testId) {
        this.testId = testId;
        return this;
    }

    public Data getData() {
        return data;
    }

    public Test setData(Data data) {
        this.data = data;
        return this;
    }

    public Api getApi() {
        return api;
    }

    public Test setApi(Api api) {
        this.api = api;
        return this;
    }

    public int getOrder() {
        return order;
    }

    public Test setOrder(int order) {
        this.order = order;
        return this;
    }

    public HashMap<String, Object> getPublish() {
        return publish;
    }

    public boolean hasPublish() {
        if(publish.size() > 0){
            return true;
        }
            return false;
    }

    public Test setPublish(HashMap<String, Object> publish) {
        this.publish.putAll(publish);
        return this;
    }

    public Test addPublish(String key,Object value) {
        this.publish.put(key,value);
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public Test setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    @JsonIgnore
    public boolean hasAssert(){
        if(anAssert != null){
            return true;
        }
        return false;
    }

    @JsonIgnore
    public String getCrudByEntity(String entity){
        try {
            Class c = ClassUtils.getClass(entity);
            List<Class<?>> superclasses = ClassUtils.getAllSuperclasses(c);
            for(HashMap<String,String> hm : crud){
                for(Map.Entry entry : hm.entrySet()) {
                    if (entry.getValue() != null) {
                        if (entry.getValue().equals(entity)) {
                            return entry.getKey().toString();
                        }
                        for (Class clazz : superclasses) {
                            if (entry.getValue().equals(clazz.getName())) {
                                return entry.getKey().toString();
                            }
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @JsonIgnore
    public boolean isCreator(String entity){
        try {
            if(entity != null) {
                Class c = ClassUtils.getClass(entity);
                List<Class<?>> superclasses = ClassUtils.getAllSuperclasses(c);
                for (HashMap<String, String> hm : crud) {
                    for (Map.Entry entry : hm.entrySet()) {
                        if (entry.getValue() != null && entry.getValue().equals(entity) && entry.getKey().equals("create")) {
                            return true;
                        }
                        for (Class clazz : superclasses) {
                            if (entry.getValue() != null && entry.getValue().equals(clazz.getName()) && entry.getKey().equals("create")) {
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
