package org.softauto.analyzer.directivs.argument;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import org.apache.commons.lang3.ClassUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Argument implements Cloneable , Serializable {


    String name;


    String type;


    List<Object> values = new ArrayList<>();


    boolean modify = true;

    String context;

    boolean entity = false;

    boolean callback = false;

    public boolean isCallback() {
        return callback;
    }

    public void setCallback(boolean callback) {
        this.callback = callback;
    }

    public boolean isEntity() {
        return entity;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setEntity(boolean entity) {
      this.entity = entity;
    }


    List<String> before = new ArrayList<>();

    public Argument(){
       // Espl.getInstance().addProperty("argument",this);
    }

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

    //@JsonIgnore
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

    /*
    public Object getFirstValue() {
       if(value != null) {
           if(value.size() > 0) {
               return value.get(0);
           }
       }
       return null;
    }


     */



    public Argument setValues(List<Object> value) {
        this.values = value;
        return this;
    }



    /*
    public Argument setValue(Object value) {
        this.value.add(value);
        return this;
    }

     */

    //@JsonSetter("value")
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