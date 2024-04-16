package org.softauto.handlers;

import org.apache.commons.lang3.StringUtils;
import org.softauto.config.Configuration;
import org.softauto.config.Context;
import org.softauto.spel.SpEL;
import soot.*;
import soot.jimple.StringConstant;
import soot.jimple.internal.*;
import soot.tagkit.LineNumberTag;
import soot.tagkit.SignatureTag;
import soot.tagkit.Tag;

import java.util.*;

public class HandleReturn {

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

    private HashMap<SootClass,SootMethod> invokeMap = new HashMap<>();

    public HashMap<SootClass,SootMethod> getInvokeMap() {
        return invokeMap;
    }

    public void addResponseChain(String clazz) {
        if(!responseChain.contains(clazz)){
            responseChain.add(clazz);
        }
    }

    public LinkedList<String> getResponseChain() {
        return responseChain;
    }

    public HandleReturn setUnboxExcludeList(List<String> unboxExcludeList) {
        this.unboxExcludeList = unboxExcludeList;
        return this;
    }

    public HandleReturn setUnboxList(List<String> unboxList) {
        this.unboxList = unboxList;
        return this;
    }

    public HandleReturn setBody(Body body) {
        this.body = body;
        return this;
    }


    public HandleReturn setResponseObject(String responseObject) {
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
        if(unboxList.contains(responseObject)) {
            if (type == null) {
                type = responseObject;
            }
            for (Unit unit : body.getUnits()) {
                for (ValueBox valueBox : unit.getUseAndDefBoxes()) {
                    if (valueBox.getValue() instanceof AbstractInvokeExpr) {
                        String refClassName = ((AbstractInvokeExpr) valueBox.getValue()).getMethodRef().getDeclaringClass().getName();
                        //String  returnClassName = ((AbstractInvokeExpr) valueBox.getValue()).getMethodRef().getReturnType().toString();
                        //String  className = ((AbstractInvokeExpr) valueBox.getValue()).getArgs().get(0).getType().toString();
                       if(!used.contains(unit.toString())){
                        used.add(unit.toString());
                        if(valueBox.getValue() instanceof JVirtualInvokeExpr && !unboxList.contains(((JVirtualInvokeExpr) valueBox.getValue()).getMethodRef().getDeclaringClass().getName()) &&((JVirtualInvokeExpr) valueBox.getValue()).getMethodRef().getDeclaringClass().getName().toString().contains(Configuration.get(Context.DOMAIN).asString()) && !((JVirtualInvokeExpr) valueBox.getValue()).getMethodRef().getReturnType().toString().equals("void") && !((JVirtualInvokeExpr) valueBox.getValue()).getMethodRef().getReturnType().toString().contains("$") && !((JVirtualInvokeExpr) valueBox.getValue()).getMethodRef().getSubSignature().toString().contains("Exception")){
                            //virtualInvokeList.add(((JVirtualInvokeExpr) valueBox.getValue()).getMethodRef().getSubSignature().toString());
                            invokeMap.put(((JVirtualInvokeExpr) valueBox.getValue()).getMethodRef().getDeclaringClass(),((JVirtualInvokeExpr) valueBox.getValue()).getMethodRef().resolve());
                            //virtualInvokeList.add(((JVirtualInvokeExpr) valueBox.getValue()).getMethodRef().getDeclaringClass().getName());
                        }
                        if(valueBox.getValue() instanceof JStaticInvokeExpr && !unboxList.contains(((JStaticInvokeExpr) valueBox.getValue()).getMethodRef().getDeclaringClass().getName()) && ((JStaticInvokeExpr) valueBox.getValue()).getMethodRef().getDeclaringClass().getName().toString().contains(Configuration.get(Context.DOMAIN).asString()) && !((JStaticInvokeExpr) valueBox.getValue()).getMethodRef().getReturnType().toString().equals("void") && !((JStaticInvokeExpr) valueBox.getValue()).getMethodRef().getReturnType().toString().contains("$") && !((JStaticInvokeExpr) valueBox.getValue()).getMethodRef().getSubSignature().toString().contains("Exception")){
                            //virtualInvokeList.add(((JStaticInvokeExpr) valueBox.getValue()).getMethodRef().getSubSignature().toString());
                            invokeMap.put(((JStaticInvokeExpr) valueBox.getValue()).getMethodRef().getDeclaringClass(),((JStaticInvokeExpr) valueBox.getValue()).getMethodRef().resolve());
                            //virtualInvokeList.add(((JStaticInvokeExpr) valueBox.getValue()).getMethodRef().getDeclaringClass().getName());
                        }
                        if (refClassName.contains(responseObject) && !ignoreUnite.contains(unit.toString())) {
                            if (unboxList.contains(responseObject)) {
                                addResponseChain(responseObject);
                            }
                            ignoreUnite.add(unit.toString());
                            if (((AbstractInvokeExpr) valueBox.getValue()).getArgs() != null && ((AbstractInvokeExpr) valueBox.getValue()).getArgs().size() > 0) {
                                for (Value value : ((AbstractInvokeExpr) valueBox.getValue()).getArgs()) {
                                    if (value instanceof StringConstant) {
                                        name = ((StringConstant) value).value;
                                        type = "java.lang.String";

                                        String resultParameterizedType = getResultParameterizedType(unit.getTags());
                                        if (resultParameterizedType != null) {
                                            this.resultParameterizedType = resultParameterizedType;
                                        }
                                    } else {
                                        name = value.toString().toString().contains("$") ? name : value.toString();
                                        type = value.getType().toString();

                                        String resultParameterizedType = getResultParameterizedType(unit.getTags());
                                        if (resultParameterizedType != null) {
                                            this.resultParameterizedType = resultParameterizedType;
                                        }
                                        if (unboxList.contains(type) || value.toString().contains("$stack")) {
                                            if (unboxList.contains(type)) {
                                                addResponseChain(type);
                                            }
                                            if(unboxList.contains(type)){
                                                responseObject = type;
                                                used = new ArrayList<>();
                                            }else{
                                                names.add(name);
                                                types.add(type);
                                            }

                                            responseObject = parser(responseObject);
                                            resultParameterizedType = getResultParameterizedType(unit.getTags());
                                            if (resultParameterizedType != null) {
                                                this.resultParameterizedType = resultParameterizedType;
                                            }
                                            //} else if (isModel(value.getType())) {
                                            //   return type;
                                        } else if (value instanceof JimpleLocal) {
                                            type = responseObject = value.getType().toString();
                                            resultParameterizedType = getResultParameterizedType(unit.getTags());
                                            if (resultParameterizedType != null) {
                                                this.resultParameterizedType = resultParameterizedType;
                                            }
                                        }
                                    }
                                }
                            } else {
                                //  name = ((AbstractInvokeExpr) valueBox.getValue()).getMethodRef().getName();
                                //  type = ((AbstractInvokeExpr) valueBox.getValue()).getMethodRef().getReturnType().toString();

                            }
                        }
                        /*
                        String  returnClassName = ((AbstractInvokeExpr) valueBox.getValue()).getMethodRef().getReturnType().toString();
                        if(unboxList.contains(returnClassName)) {
                           if(((AbstractInvokeExpr) valueBox.getValue()).getArgs().size() > 0) {
                               //for(Value v : ((AbstractInvokeExpr) valueBox.getValue()).getArgs()){
                               for(int i=0;i< ((AbstractInvokeExpr) valueBox.getValue()).getArgs().size();i++){
                                   Value v =  ((AbstractInvokeExpr) valueBox.getValue()).getArgs().get(i);
                                   if (v instanceof StringConstant){
                                       name = ((AbstractInvokeExpr) valueBox.getValue()).getArgBox(i).getValue().toString();
                                       //type = ((AbstractInvokeExpr) valueBox.getValue()).getArgBox(0).getValue().getType().toString();
                                   }
                                   if(v instanceof JimpleLocal){
                                       if(!((AbstractInvokeExpr) valueBox.getValue()).getArgBox(i).getValue().toString().contains("stack")) {
                                           name = ((AbstractInvokeExpr) valueBox.getValue()).getArgBox(i).getValue().toString();
                                       }
                                       type =  ((AbstractInvokeExpr) valueBox.getValue()).getArgBox(i).getValue().getType().toString();
                                   }
                               }

                               //name = ((AbstractInvokeExpr) valueBox.getValue()).getArgBox(0).getValue().toString();
                               //type = ((AbstractInvokeExpr) valueBox.getValue()).getArgBox(0).getValue().getType().toString();

                           }
                        }


                         */
                        }
                    }
                }
            }
        }
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
