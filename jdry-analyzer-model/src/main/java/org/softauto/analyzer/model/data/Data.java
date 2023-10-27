package org.softauto.analyzer.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.softauto.analyzer.directivs.request.Request;
import org.softauto.analyzer.directivs.result.Result;

import java.io.Serializable;
import java.util.HashMap;


public class Data implements Cloneable, Serializable {


    protected int id = -1;


    protected Request request;

    protected Result response;

    protected String clazz;

    protected String plugin;

    protected String method;

    protected long thread  = - 1;

    protected long time = -1;

    protected String scenarioId;

    protected boolean used = false;



    protected HashMap<String, Object> pluginData = new HashMap<>();

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public int getId() {
        return id;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    HashMap<String, Object> threadLocal = new HashMap<>();

    public HashMap<String, Object> getThreadLocal() {
        return threadLocal;
    }

    public void setThreadLocal(HashMap<String, Object> threadLocal) {
        this.threadLocal = threadLocal;
    }

    public Data setId(int id) {
        this.id = id;
        return this;
    }

    public Request getRequest() {
        return request;
    }

    @JsonSetter(nulls = Nulls.SKIP)
    public Data setRequest(Request request) {
        this.request = request;
        return this;
    }

    public Result getResponse() {
        return response;
    }

    public Data setResponse(Result response) {
        this.response = response;
        return this;
    }

    public String getClazz() {
        return clazz;
    }

    public Data setClazz(String clazz) {
        this.clazz = clazz;
        return this;
    }

    public String getPlugin() {
        return plugin;
    }

    public Data setPlugin(String plugin) {
        this.plugin = plugin;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public Data setMethod(String method) {
        this.method = method;
        return this;
    }

    public long getThread() {
        return thread;
    }

    public Data setThread(long thread) {
        this.thread = thread;
        return this;
    }

    public long getTime() {
        return time;
    }

    public Data setTime(long time) {
        this.time = time;
        return this;
    }

    public HashMap<String, Object> getPluginData() {
        return pluginData;
    }

    public Data setPluginData(HashMap<String, Object> pluginData) {
        this.pluginData.putAll(pluginData);
        return this;
    }

    @JsonIgnore
    public String getCallOption(){
        try {
            String str = new ObjectMapper().writeValueAsString(pluginData);
            return str.replace("\\", "\\\\").replace("\"", "\\\"");

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
