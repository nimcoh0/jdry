package org.softauto.analyzer.model.after;

import java.io.Serializable;

public class After implements Cloneable, Serializable {

    private Object expression;

    private String type;

    private String name;

    private String parentResultName;

    private String parentName;

    public String getParentResultName() {
        return parentResultName;
    }

    public void setParentResultName(String parentResultName) {
        this.parentResultName = parentResultName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Object getExpression() {
        return expression;
    }

    public After setExpression(Object expression) {
        this.expression = expression;
        return this;
    }

    public String getType() {
        return type;
    }

    public After setType(String type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public After setName(String name) {
        this.name = name;
        return this;
    }

    public boolean hasExpression(){
        if(expression != null){
            return true;
        }
        return false;
    }

    public boolean hasType(){
        if(type != null){
            return true;
        }
        return false;
    }

    public boolean hasName(){
        if(name != null){
            return true;
        }
        return false;
    }


}
