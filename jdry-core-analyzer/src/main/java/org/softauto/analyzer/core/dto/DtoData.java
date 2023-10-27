package org.softauto.analyzer.core.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DtoData {
    int id;

    List<Object> args = new ArrayList<>();

    String returnType;

    List<String> types = new ArrayList<>();

    List<String> names =  new ArrayList<>();

    String clazz;

    String plugin;

    String method;

    String result;

    long thread;

    long time;

    boolean found = false;

    String transactionId;

    String instance;

    HashMap<String, Object> pluginData = new HashMap<>();

    HashMap<String,Object> props = new HashMap<>();

    HashMap<String, Object> threadLocal = new HashMap<>();

    public HashMap<String, Object> getThreadLocal() {
        return threadLocal;
    }

    public void setThreadLocal(HashMap<String, Object> threadLocal) {
        this.threadLocal = threadLocal;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public HashMap<String, Object> getProps() {
        return props;
    }

    public HashMap<String, Object> getPluginData() {
        return pluginData;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }




    public void setPluginData(HashMap<String, Object> pluginData) {
        this.pluginData = pluginData;
    }

    public void setProps(HashMap<String, Object> props) {
        this.props = props;
    }

    public void addProp(String key,Object value){
        props.put(key,value);
    }

    public long getThread() {
        return thread;
    }

    public void setThread(long thread) {
        this.thread = thread;
    }

    public List<Object> getArgs() {
        return args;
    }

    public void setArgs(List<Object> args) {
        this.args = args;
    }

    public void addArgs(String arg) {
        this.args.add(arg);
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public void addType(String type) {
        this.types.add(type);
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object getResult() {
        return result;
    }



}


