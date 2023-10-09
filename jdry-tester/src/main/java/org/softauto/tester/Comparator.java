package org.softauto.tester;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.softauto.core.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class Comparator<Object> {


    Object expected;

    Object actual;

    ObjectMapper objectMapper = new ObjectMapper();


    public Comparator setExpected(Object expected) {
        this.expected = expected;
        return this;
    }

    public Comparator setActual(Object actual) {
        this.actual = actual;
        return this;
    }

    private JsonNode parse(Object o){
        try {
            String s = objectMapper.writeValueAsString(o);
            return objectMapper.readTree(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> getIgnorableFieldNames(Class clazz){
        List<String> ignoreList = new ArrayList<>();
        if(clazz.getGenericSuperclass() != null){
            Class c = clazz.getSuperclass();
            ignoreList.addAll(getIgnorableFieldNames(c));
        }
        /*
        Field[] fileds = clazz.getDeclaredFields();
        for(Field field : fileds){
           field.setAccessible(true);
           if(field.isAnnotationPresent(org.softauto.annotations.UpdateForTesting.class)){
               ignoreList.add(field.getName());
           }
        }

         */
        return ignoreList;
    }


    private String getFilterJson(Object obj,String[] ignoreFilter){
        try {
            objectMapper.addMixIn(obj.getClass(), PropertyFilterMixIn.class);
            FilterProvider filters = new SimpleFilterProvider().addFilter("filter properties by name",
                    SimpleBeanPropertyFilter.serializeAllExcept(ignoreFilter));
            ObjectWriter writer = objectMapper.writer(filters);
            return writer.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @JsonFilter("filter properties by name")
    public static class PropertyFilterMixIn {}

    public boolean compare(){
        try {
            if(expected != null && actual != null) {
                if (Utils.isPrimitive(expected.toString()) || (expected instanceof String && !Utils.isJson(expected.toString()))) {
                    if (expected.equals(actual)) {
                        return true;
                    }
                } else {
                    List<String> expectedIgnoreFieldList = getIgnorableFieldNames(expected.getClass());
                    List<String> actualIgnoreFieldList = getIgnorableFieldNames(actual.getClass());
                    String expectedJson = getFilterJson(expected, expectedIgnoreFieldList.toArray(new String[expectedIgnoreFieldList.size()]));
                    String actualJson = getFilterJson(actual, actualIgnoreFieldList.toArray(new String[actualIgnoreFieldList.size()]));
                    if (objectMapper.readTree(expectedJson).equals(objectMapper.readTree(actualJson))) {
                        return true;
                    }
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return false;
    }



}
