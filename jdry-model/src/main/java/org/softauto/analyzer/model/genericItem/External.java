package org.softauto.analyzer.model.genericItem;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class External implements   Cloneable, Serializable {

    private String mode;

    private String clazz;

    private String target;

    private String method;

    private String name;

    private String type;

    private LinkedList<String> types = new LinkedList<>();

    private LinkedList<String> args = new LinkedList<>();

    private String result;

    private String sootType;

    private boolean _static;

    public String getSootType() {
        return sootType;
    }

    public boolean isStatic() {
        return _static;
    }

    public void setStatic(boolean _static) {
        this._static = _static;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSootType(String sootType) {
        this.sootType = sootType;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<String> getArgs() {
        return args;
    }

    public External setArgs(LinkedList<String> args) {
        this.args = args;
        return this;
    }



    public String getMode() {
        return mode;
    }

    public External setMode(String mode) {
        this.mode = mode;
        return this;
    }

    public String getClazz() {
        return clazz;
    }

    public External setClazz(String clazz) {
        this.clazz = clazz;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public External setMethod(String method) {
        this.method = method;
        return this;
    }

    public List<String> getTypes() {
        return types;
    }

    public External setTypes(LinkedList<String> types) {
        this.types = types;
        return this;
    }

    public External setType(String type) {
        this.type = type;
        return this;
    }

    public String getResult() {
        return result;
    }

    public External setResult(String result) {
        this.result = result;
        return this;
    }
}
