package org.softauto.analyzer.model.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.softauto.analyzer.directivs.request.Request;
import org.softauto.analyzer.directivs.result.Result;
import org.softauto.analyzer.model.Item;

public class Listener implements Item {


    protected Object expression;

    protected long time;

    protected String callback;

    protected String method;

    protected String namespce;

    protected String description;

    protected Request request;

    protected Result response;

    protected String fullName;

    protected String id;

    protected String discoveryId;

    public Object getExpression() {
        return expression;
    }

    public Listener setExpression(Object expression) {
        this.expression = expression;
        return this;
    }

    public long getTime() {
        return time;
    }

    public Listener setTime(long time) {
        this.time = time;
        return this;
    }

    public String getCallback() {
        return callback;
    }

    public Listener setCallback(String callback) {
        this.callback = callback;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public Listener setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getNamespce() {
        return namespce;
    }

    public Listener setNamespce(String namespce) {
        this.namespce = namespce;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Listener setDescription(String description) {
        this.description = description;
        return this;
    }

    public Request getRequest() {
        return request;
    }

    public Listener setRequest(Request request) {
        this.request = request;
        return this;
    }

    public Result getResponse() {
        return response;
    }

    public Listener setResponse(Result response) {
        this.response = response;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public Listener setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getId() {
        return id;
    }

    public Listener setId(String id) {
        this.id = id;
        return this;
    }

    public String getDiscoveryId() {
        return discoveryId;
    }

    public Listener setDiscoveryId(String discoveryId) {
        this.discoveryId = discoveryId;
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

    public boolean hasExpression(){
        if(expression != null){
            return true;
        }
        return false;
    }

}
