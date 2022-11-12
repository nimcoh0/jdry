package org.softauto.serializer.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class Message {

    public static Builder newBuilder() { return new Builder();}

    private String descriptor;
    private Class[] types;
    private Object[] args;
    private String message;
    private String threadId;
    private String service;
    private Map<String , Object> data = new HashMap<>();
    private String state;
    private MessageType messageType = MessageType.METHOD;


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


    public static class Builder {
        private String descriptor;
        private Class[] types;
        private Object[] args;
        private String message;
        private String threadId;
        private String service;
        private Map<String , Object> data = new HashMap<>();
        private String state;
        private MessageType messageType = MessageType.METHOD;

        public String getState() {
            return state;
        }

        public Builder setType(MessageType messageType) {
            this.messageType = messageType;
            return this;
        }

        public Builder setState(String state) {
            this.state = state;
            return this;
        }

        public Builder setService(String service) {
            this.service = service;
            return this;
        }

        public Builder setThreadId(String threadId) {
            this.threadId = threadId;
            return this;
        }

        public Builder addData(String key, Object value){
            data.put(key,value);
            return this;
        }

        public Builder setDescriptor(String descriptor) {
            this.descriptor = descriptor;
            return this;
        }

        public Builder setTypes(Class[] types) {
            this.types = types;
            return this;
        }

        public Builder setArgs(Object[] args) {
            this.args = args;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setData(Map<String, Object> data) {
            this.data = data;
            return this;
        }

        public Message build(){
            Message msg =   new Message() ;
            msg.args = args;
            msg.data = data;
            msg.descriptor = descriptor;
            msg.types = types;
            msg.message = message;
            msg.threadId = threadId;
            msg.service = service;
            msg.state = state;
            msg.messageType = messageType;
            return msg;
        }
    }
}
