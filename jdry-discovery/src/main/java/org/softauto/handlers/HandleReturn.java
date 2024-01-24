package org.softauto.handlers;

import org.softauto.config.Configuration;
import org.softauto.spel.SpEL;
import soot.*;
import soot.jimple.StringConstant;
import soot.jimple.internal.*;

import java.util.*;
import java.util.stream.Collectors;

public class HandleReturn {

    private String responseObject;

    private List<String> unboxExcludeList;

    private List<String> unboxList;

    private Body body;

    private  String type;

    private String name;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    private LinkedList<String> responseChain = new LinkedList<>();

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

    public String parser(String responseObject) {
        if(type == null){
            type = responseObject;
        }
        for(Unit unit : body.getUnits()){
            for(ValueBox valueBox : unit.getUseAndDefBoxes()) {
                if (valueBox.getValue() instanceof AbstractInvokeExpr) {
                    String  refClassName = ((AbstractInvokeExpr) valueBox.getValue()).getMethodRef().getDeclaringClass().getName();
                    //String  returnClassName = ((AbstractInvokeExpr) valueBox.getValue()).getMethodRef().getReturnType().toString();
                    //String  className = ((AbstractInvokeExpr) valueBox.getValue()).getArgs().get(0).getType().toString();
                    if (refClassName.contains(responseObject)  && !ignoreUnite.contains(unit.toString()) ) {
                        if(unboxList.contains(responseObject)) {
                            addResponseChain(responseObject);
                        }
                        ignoreUnite.add(unit.toString());
                        if (((AbstractInvokeExpr) valueBox.getValue()).getArgs() != null && ((AbstractInvokeExpr) valueBox.getValue()).getArgs().size() > 0) {
                            for(Value value : ((AbstractInvokeExpr) valueBox.getValue()).getArgs()) {
                                if(value instanceof StringConstant) {
                                    name = ((StringConstant) value).value;
                                    type = "java.lang.String";
                                } else {
                                    name = value.toString();
                                    type = value.getType().toString();

                                    if (unboxList.contains(type) || name.contains("$stack")) {
                                        type = responseObject = parser(type);
                                    //} else if (isModel(value.getType())) {
                                     //   return type;
                                    } else if (value instanceof JimpleLocal) {
                                        type = responseObject = value.getType().toString();
                                    }
                                }
                             }
                        }else {
                          //  name = ((AbstractInvokeExpr) valueBox.getValue()).getMethodRef().getName();
                          //  type = ((AbstractInvokeExpr) valueBox.getValue()).getMethodRef().getReturnType().toString();

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
        return responseObject;
    }

}
