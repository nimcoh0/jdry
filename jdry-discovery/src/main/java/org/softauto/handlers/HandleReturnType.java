package org.softauto.handlers;

import org.softauto.core.Configuration;
import org.softauto.spel.SpEL;
import soot.*;
import soot.jimple.internal.*;

import java.util.List;

public class HandleReturnType {

    private String responseObject;

    private List<String> unboxExcludeList;

    private List<String> unboxList;

    private Body body;

    public HandleReturnType setUnboxExcludeList(List<String> unboxExcludeList) {
        this.unboxExcludeList = unboxExcludeList;
        return this;
    }

    public HandleReturnType setUnboxList(List<String> unboxList) {
        this.unboxList = unboxList;
        return this;
    }

    public HandleReturnType setBody(Body body) {
        this.body = body;
        return this;
    }


    public HandleReturnType setResponseObject(String responseObject) {
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
        for(Unit unit : body.getUnits().getElementsUnsorted()){
            for(ValueBox valueBox : unit.getUseBoxes()) {
                if (valueBox.getValue() instanceof AbstractInvokeExpr) {
                    String className = ((AbstractInvokeExpr) valueBox.getValue()).getMethodRef().getDeclaringClass().getName();
                    if (className.contains(responseObject)) {
                        if (((AbstractInvokeExpr) valueBox.getValue()).getArgs() != null && ((AbstractInvokeExpr) valueBox.getValue()).getArgs().size() > 0) {
                            for(Value value : ((AbstractInvokeExpr) valueBox.getValue()).getArgs()) {
                                String type = value.getType().toString();
                                String name = value.toString();
                                if (unboxList.contains(type)) {
                                    responseObject = parser(type);
                                }else if(isModel(value.getType().getDefaultFinalType())){
                                    return type;
                                }else if(value instanceof JimpleLocal ){
                                    responseObject = value.getType().toString();
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
