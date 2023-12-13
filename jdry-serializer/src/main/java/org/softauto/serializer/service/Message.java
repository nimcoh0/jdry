package org.softauto.serializer.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class Message {


    /**
     * message descriptor . in case of a method it will be method full name
     */
    private String descriptor;

    /**
     * arguments type
     */
    private Class[] types;

    /**
     * arguments value
     */
    private Object[] args;

    /**
     * entity
     */
    private String message;

    @Deprecated
    private String threadId;

    @Deprecated
    private String service;

    /**
     * scenarion id
     */
    private String scenarioId;

    /**
     * any extra data
     */
    private Map<String , Object> data = new HashMap<>();


    private String state;


    private MessageType messageType = MessageType.METHOD;


    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getState() {
        return state;
    }

    public String getService() {
        return service;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getThreadId() {
        return threadId;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public Class[] getTypes() {
        return types;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public boolean hasKey(String key){
        if(data.containsKey(key)){
            return true;
        }
        return false;
    }

    public Object getData(String key){
        if(hasKey(key)) {
            return data.get(key);
        }
        return null;
    }


    public String toJson(){
        try {
          return new ObjectMapper().writeValueAsString(this);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public void setTypes(Class[] types) {
        this.types = types;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}
