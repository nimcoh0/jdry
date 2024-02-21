package org.softauto.analyzer.model.genericItem;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class External implements   Cloneable, Serializable {

    private int id=0;

    private String mode;

    private String clazz;

    private String target;

    private String method;

    private String name;

    private String type;

    private String callMethod;

    private String crud;

    private boolean api = false;

    private String apiClass;

    private String apiMethod;

    private int number;

    private String instance;

    private int assignFrom;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getAssignFrom() {
        return assignFrom;
    }

    public void setAssignFrom(int assignFrom) {
        this.assignFrom = assignFrom;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApiClass() {
        return apiClass;
    }

    public void setApiClass(String apiClass) {
        this.apiClass = apiClass;
    }

    public String getApiMethod() {
        return apiMethod;
    }

    public void setApiMethod(String apiMethod) {
        this.apiMethod = apiMethod;
    }

    private LinkedList<String> types = new LinkedList<>();

    private LinkedList<String> args = new LinkedList<>();

    private String result;

    private String sootType;

    private boolean _static;

    public boolean isApi() {
        return api;
    }

    public void setApi(boolean api) {
        this.api = api;
    }

    public String getCrud() {
        return crud;
    }

    public void setCrud(String crud) {
        this.crud = crud;
    }

    public String getCallMethod() {
        return callMethod;
    }

    public void setCallMethod(String callMethod) {
        this.callMethod = callMethod;
    }

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
