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
        return this;
    }

    private boolean isModel(Type type){
        if(Configuration.has("entity_identify")){
           String schema =  Configuration.get("entity_identify").asString();
            return (Boolean) SpEL.getInstance().addProperty("type",type).evaluate(schema);
        }
        return false;
    }



    public String parser(String responseObject) {
        for(Unit unit : body.getUnits()){
            for(ValueBox valueBox : unit.getUseAndDefBoxes()) {
                if (valueBox.getValue() instanceof AbstractInvokeExpr) {
                    String  className = ((AbstractInvokeExpr) valueBox.getValue()).getMethodRef().getDeclaringClass().getName();
                    if (className.contains(responseObject)) {
                        if(unboxList.contains(responseObject)) {
                            addResponseChain(responseObject);
                        }
                        if (((AbstractInvokeExpr) valueBox.getValue()).getArgs() != null && ((AbstractInvokeExpr) valueBox.getValue()).getArgs().size() > 0) {
                            for(Value value : ((AbstractInvokeExpr) valueBox.getValue()).getArgs()) {
                                if(value instanceof StringConstant) {
                                    name = ((StringConstant) value).value;
                                    type = "java.lang.String";
                                } else {
                                    name = value.toString();
                                    type = value.getType().toString();

                                    if (unboxList.contains(type) || name.contains("$stack")) {
                                        responseObject = parser(type);
                                    } else if (isModel(value.getType())) {
                                        return type;
                                    } else if (value instanceof JimpleLocal) {
                                        responseObject = value.getType().toString();
                                    }
                                }
                             }
                        }
                    }
                }
            }
        }
        return responseObject;
    }

}
