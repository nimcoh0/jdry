package org.softauto.analyzer.model.argument;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import org.apache.commons.lang3.ClassUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * method call argument of return argument
 */
public class Argument implements Cloneable , Serializable {


    /**
     * argument name
     */
    private String name;


    /**
     * argument type
     */
    private String type;


    /**
     * argument value
     */
    private List<Object> values = new ArrayList<>();

    private String parameterizedType;

    private boolean modify = true;

    private Set<String> context = new HashSet<>();

    private String crud;

    private String publishName;

    /**
     * is type entity ?
     */
    private boolean entity = false;

    /**
     * is callback
     */
    private boolean callback = false;

    private boolean generic = false;

    public String getPublishName() {
        return publishName;
    }

    public void setPublishName(String publishName) {
        this.publishName = publishName;
    }

    public String getParameterizedType() {
        return parameterizedType;
    }

    public void setParameterizedType(String parameterizedType) {
        this.parameterizedType = parameterizedType;
    }

    public boolean isGeneric() {
        return generic;
    }

    public void setGeneric(boolean generic) {
        this.generic = generic;
    }

    public String getCrud() {
        return crud;
    }

    public void setCrud(String crud) {
        this.crud = crud;
    }

    public boolean isCallback() {
        return callback;
    }

    public void setCallback(boolean callback) {
        this.callback = callback;
    }

    public boolean isEntity() {
        return entity;
    }

    public Set<String>  getContext() {
        return context;
    }

    public void setContext(Set<String> context) {
        this.context = context;
    }

    public void addContext(String context) {
        this.context.add(context);
    }

    public void setEntity(boolean entity) {
      this.entity = entity;
    }


    List<String> before = new ArrayList<>();

    public Argument(){

    }

    /**
     * check if this type class is child of
     * @param type
     * @return
     */
    public boolean isInheritFrom(String type){
        try {
            Class c = ClassUtils.getClass(this.type);
            List<Class<?>> superclasses = ClassUtils.getAllSuperclasses(c);
            for(Class clazz : superclasses){
                if(clazz.getTypeName().equals(type)){
                    return true;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> getBefore() {
        return before;
    }

    public void setBefore(List<String> before) {
        this.before = before;
    }

    public void addBefore(String before) {
        this.before.add(before);
    }

    public String getName() {
        return name;
    }

    public Argument setModify(boolean modify) {
        this.modify = modify;
        return this;
    }

    @JsonSetter(nulls = Nulls.SKIP)
    public Argument setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public Argument setType(String type) {
        this.type = type;
        return this;
    }


    public List<Object> getValues(){
           return values;
    }

    @JsonIgnore
    public Object getValue(){
        if(values.size()>0) {
            return values.get(0);
        }
        return null;
    }


    @JsonIgnore
    public Object getValue(int index){
        if(values.size()>0) {
            return values.get(index);
        }
        return null;
    }



    public Argument setValues(List<Object> value) {
        this.values = value;
        return this;
    }



    public Argument addValue(Object value) {
        this.values.add(value);
        return this;
    }

    @JsonIgnore
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @JsonIgnore
    public boolean hasType(){
        if(type != null){
            return true;
        }
        return false;
    }

    @JsonIgnore
    public boolean hasValue(){
        if(values != null && values.size() > 0){
            return true;
        }
        return false;
    }

    public boolean hasBefore(){
        if(before != null){
            return true;
        }
        return false;
    }
}
