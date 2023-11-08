package org.softauto.serializer.service;

import java.util.HashMap;
import java.util.Map;

public class MessageBuilder {

    public static Builder newBuilder() { return new Builder();}

    Message message;

    public MessageBuilder(Message message){
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }


    public static class Builder {
        private String descriptor;
        private Class[] types;
        private Object[] args;
        private String message;
        private String threadId;
        private String service;
        private String scenarioId;
        private Map<String, Object> data = new HashMap<>();
        private String state;
        private MessageType messageType = MessageType.METHOD;

        public String getState() {
            return state;
        }

        public Builder setScenarioId(String scenarioId) {
            this.scenarioId = scenarioId;
            return this;
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

        public Builder addData(String key, Object value) {
            data.put(key, value);
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

        public MessageBuilder build() {
            Message msg = new Message();
            msg.setArgs(args);
            msg.setData(data);
            msg.setDescriptor(descriptor);
            msg.setTypes(types);
            msg.setMessage(message);
            msg.setThreadId(threadId);
            msg.setService(service);
            msg.setState(state);
            msg.setMessageType(messageType);
            msg.setScenarioId(scenarioId);
            return new MessageBuilder(msg);
        }
    }
}
