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


public class Api implements Item {

    protected String protocol = "RPC";

    protected String messageType = "method";

    protected CallBack callback;

    protected String role;

    protected String method;

    protected String namespace;

    protected String description;

    protected Request request;

    protected Result response;

    protected long time;

    protected String fullName;

    protected String id;

    String discoveryId;

    protected String classType ;



    protected LinkedList<After> afterList = new LinkedList();

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

    public String getMessageType() {
        return messageType;
    }

    public Api setMessageType(String messageType) {
        this.messageType = messageType;
        return this;
    }

    public CallBack getCallback() {
        return callback;
    }

    public Api setCallback(CallBack callback) {
        this.callback = callback;
        return this;
    }

    public String getRole() {
        return role;
    }

    public Api setRole(String role) {
        this.role = role;
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

    public long getTime() {
        return time;
    }

    public Api setTime(long time) {
        this.time = time;
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

    public String getDiscoveryId() {
        return discoveryId;
    }

    public Api setDiscoveryId(String discoveryId) {
        this.discoveryId = discoveryId;
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
                // suite.getListener(o.)
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
                //if(o instanceof Listener ){
                   listeners.add(listener);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  listeners;
    }



    /*
    @JsonIgnore
    public boolean isAuth(){
        if(role != null && role.equals(Role.AUTH.name())){
            return true;
        }
        return false;
    }

     */
}
