package org.softauto.jaxrs.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import org.softauto.core.AbstractCallOption;
import java.util.HashMap;

public class WebCallOption extends AbstractCallOption {

    private HashMap<String,Object> callOptions;

    private Object[] args;

    private Class[] types;

    private MediaType produce;

    private MediaType consume;

    private String fullMethodName;

    private String httpMethod;

    private String scenarioId;

    public HashMap<String, Object> getCallOptions() {
        return callOptions;
    }

    public WebCallOption setCallOptions(HashMap<String, Object> callOptions) {
        this.callOptions = callOptions;
        return this;
    }

    public Object[] getArgs() {
        return args;
    }

    public WebCallOption setArgs(Object[] args) {
        this.args = args;
        return this;
    }

    public Class[] getTypes() {
        return types;
    }

    public WebCallOption setTypes(Class[] types) {
        this.types = types;
        return this;
    }

    public MediaType getProduce() {
        return produce;
    }

    public WebCallOption setProduce(MediaType produce) {
        this.produce = produce;
        return this;
    }

    public MediaType getConsume() {
        return consume;
    }

    public WebCallOption setConsume(MediaType consume) {
        this.consume = consume;
        return this;
    }

    public String getFullMethodName() {
        return fullMethodName;
    }

    public WebCallOption setFullMethodName(String fullMethodName) {
        this.fullMethodName = fullMethodName;
        return this;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public WebCallOption setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public WebCallOption setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
        return this;
    }

    @Override
    public HashMap<String,Object> toMap() {
        try {
            String json =  new ObjectMapper().writeValueAsString(this);
            return new ObjectMapper().readValue(json,HashMap.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
