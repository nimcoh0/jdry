package org.softauto.handlers;

import org.apache.commons.lang3.StringUtils;
import org.softauto.config.Configuration;
import org.softauto.spel.SpEL;
import soot.*;
import soot.jimple.StringConstant;
import soot.jimple.internal.AbstractInvokeExpr;
import soot.jimple.internal.JVirtualInvokeExpr;
import soot.jimple.internal.JimpleLocal;
import soot.tagkit.SignatureTag;
import soot.tagkit.Tag;

import java.util.*;

public class HandleReturn1 {

    private String responseObject;

    private List<String> unboxExcludeList;

    private List<String> unboxList;

    private Body body;

    private String name;

    private String type;

    private  Set<String> types = new HashSet<>();

    private Set<String> names = new HashSet<>();

    private String resultParameterizedType;

    public String getResultParameterizedType() {
        return resultParameterizedType;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Set<String> getTypes() {
        return types;
    }

    public Set<String> getNames() {
        return names;
    }

    private LinkedList<String> responseChain = new LinkedList<>();

    private Set<String> virtualInvokeList = new HashSet<>();

    public Set<String> getVirtualInvokeList() {
        return virtualInvokeList;
    }

    public void addResponseChain(String clazz) {
        if(!responseChain.contains(clazz)){
            responseChain.add(clazz);
        }
    }

    public LinkedList<String> getResponseChain() {
        return responseChain;
    }

    public HandleReturn1 setUnboxExcludeList(List<String> unboxExcludeList) {
        this.unboxExcludeList = unboxExcludeList;
        return this;
    }

    public HandleReturn1 setUnboxList(List<String> unboxList) {
        this.unboxList = unboxList;
        return this;
    }

    public HandleReturn1 setBody(Body body) {
        this.body = body;
        return this;
    }


    public HandleReturn1 setResponseObject(String responseObject) {
        this.responseObject = responseObject;
        this.type = responseObject;
        return this;
    }

    private boolean isModel(Type type){
        if(Configuration.has("entity_identify")){
           String schema =  Configuration.get("entity_identify").asString();
            return (Boolean) SpEL.getInstance().addProperty("type",type).evaluate(schema);
        }
        return false;
    }

    List<String> ignoreUnite = new ArrayList<>();
    List<String> used = new ArrayList<>();



    public String parser(String responseObject) {
       // if(unboxList.contains(responseObject)) {
            if (type == null) {
                type = responseObject;
            }
            for (Unit unit : body.getUnits()) {
                for (ValueBox valueBox : unit.getUseAndDefBoxes()) {
                    if (valueBox.getValue() instanceof AbstractInvokeExpr) {
                        String refClassName = ((AbstractInvokeExpr) valueBox.getValue()).getMethodRef().getDeclaringClass().getName();
                        if(((AbstractInvokeExpr) valueBox.getValue()).getMethodRef().getReturnType().toString().equals(responseObject)){
                           return refClassName;
                       }
                    }
                }
           }
           // }
       // }
        return responseObject;
    }

    public String getResultParameterizedType(List<Tag> tags) {
        if(tags != null && tags.size() > 0) {
            for (Tag tag : tags) {
                if (tag instanceof SignatureTag) {
                    SignatureTag t = (SignatureTag) tag;
                    String string =  StringUtils.substringBetween(t.getSignature(),"<",">");
                    if(string != null) {
                        return string.substring(1, string.length() - 1).replace("/", ".");
                    }
                }
            }
        }
        return null;
    }






    private LinkedList<String> setArgs(List<Value> args) {
        LinkedList<String> list = new LinkedList<>();
        for(Value value : args){
            list.add(value.toString());
        }
        return list;
    }

    private LinkedList<String> setTypes(List<Type> types) {
        LinkedList<String> list = new LinkedList<>();
        for(Type type : types){
            list.add(type.toString());
        }
        return list;
    }

}
