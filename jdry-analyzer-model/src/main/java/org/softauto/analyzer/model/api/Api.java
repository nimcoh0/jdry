package org.softauto.analyzer.model.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.softauto.analyzer.directivs.callback.CallBack;
import org.softauto.analyzer.directivs.request.Request;
import org.softauto.analyzer.directivs.result.Result;
import org.softauto.analyzer.model.Item;
import org.softauto.analyzer.model.after.After;
import org.softauto.analyzer.model.listener.Listener;
import org.softauto.analyzer.model.suite.Suite;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * api definition
 */
public class Api implements Item {

    /**
     * the protocol to be use by this api . default GRPC
     */
    protected String protocol = "RPC";

    /**
     * the callBack variable details (if callback is used )
     */
    protected CallBack callback;

    /**
     * the method name to be invoke
     */
    protected String method;

    /**
     * the method full class name
     */
    protected String namespace;

    /**
     * api description
     */
    protected String description;

    /**
     * list of @arguments contain method args name/type/value
     */
    protected Request request;

    /**
     * response @argument contain name/type/value
     */
    protected Result response;

    /**
     * api method full name as package + class + method
     * with "_" insted of "."
     */
    protected String fullName;

    /**
     * api id (default method name)
     */
    protected String id;

    /**
     * indicate if and how to initialize a class
     */
    protected String classType ;

    /**
     * a list of @after action's to be executed after the api call
     */
    protected LinkedList<After> afterList = new LinkedList();

    /**
     * a list of interest methods to be call by the api as part of the api flow
     * interest method mean method that include interest annotation like ListenerForTesting
     */
    protected LinkedList<Object> childes = new LinkedList<>();

    public LinkedList<Object> getChildes() {
        return childes;
    }

    public Api setChildes(LinkedList<Object> childes) {
        this.childes = childes;
        return this;
    }

    public Api addChild(Object child) {
        if(child != null) {
            this.childes.add(child);
        }
        return this;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getClassType() {
        return classType;
    }

    public Api setClassType(String classType) {
        this.classType = classType;
        return this;
    }

    public Api setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public CallBack getCallback() {
        return callback;
    }

    public Api setCallback(CallBack callback) {
        this.callback = callback;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public Api setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getNamespace() {
        return namespace;
    }

    public Api setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Api setDescription(String description) {
        this.description = description;
        return this;
    }

    public Request getRequest() {
        return request;
    }

    public Api setRequest(Request request) {
        this.request = request;
        return this;
    }

    public Result getResponse() {
        return response;
    }

    public Api setResponse(Result response) {
        this.response = response;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public Api setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getId() {
        return id;
    }

    public Api setId(String id) {
        this.id = id;
        return this;
    }

    public List<After> getAfterList() {
        return afterList;
    }

    public Api setAfterList(LinkedList<After> afterList) {
        this.afterList = afterList;
        return this;
    }

    public Api addAfter(After after) {
        this.afterList.add(after);
        return this;
    }

    public JsonNode toJson(){
        try {
            String s =  new ObjectMapper().writeValueAsString(this);
            return new ObjectMapper().readTree(s);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String toString(){
        try {
            return  new ObjectMapper().writeValueAsString(this);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @JsonIgnore
    public boolean hasAfter(){
        if(afterList != null && afterList.size() > 0){
            return true;
        }
        return false;
    }

    @JsonIgnore
    public List<Listener> getListeners(){
        List<Listener> listeners = new ArrayList<>();
        try {
            for(Object o : childes){
                if(o instanceof Listener ){
                    listeners.add((Listener)o);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  listeners;
    }

    @JsonIgnore
    public List<Listener> getListeners(Suite suite){
        List<Listener> listeners = new ArrayList<>();
        try {
            for(Object o : childes){
                Listener listener = suite.getListener(((Map)o).get("namespce")+"."+((Map)o).get("name"));
                if(listener != null){
                   listeners.add(listener);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  listeners;
    }
}
