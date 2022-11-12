package org.softauto.jaxrs;

import java.util.HashMap;
import java.util.Map;

public class CallOptions implements org.softauto.core.CallOptions {

    public static Builder newBuilder() { return new Builder();}

    HashMap<String, Object> options = null;

    public CallOptions(HashMap<String, Object> options){
        this.options = options;
    }

    @Override
    public HashMap<String, Object> getOptions() {
        return options;
    }



    public static class Builder {

        javax.ws.rs.core.MultivaluedMap<String, Map<String,String>> parameters = new javax.ws.rs.core.MultivaluedHashMap();
        javax.ws.rs.core.MultivaluedMap<String, Object> headers = new javax.ws.rs.core.MultivaluedHashMap<>();
        javax.ws.rs.core.MediaType produce =  javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
        javax.ws.rs.core.MediaType consume =  javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
        HashMap<String ,Object> properties = new HashMap<>();
        org.glassfish.jersey.client.authentication.HttpAuthenticationFeature feature  = null;


        public Builder addParameters(javax.ws.rs.core.MultivaluedMap<String, Map<String,String>> parameters){
            this.parameters = parameters;
            return this;
        }

        public Builder addParameter(ParamType paramType, String key, String value){
            Map<String,String> m = new HashMap<>();
            m.put(key,value);
            parameters.add(paramType.name(),m);
            return this;
        }

        public Builder addHeader(String key,String value){
            headers.add(key,value);
            return this;
        }

        public Builder addHeaders(javax.ws.rs.core.MultivaluedMap<String, Object> headers){
            this.headers = headers;
            return this;
        }

        public Builder setProduce(javax.ws.rs.core.MediaType mediaType){
            this.produce = mediaType;
            return this;
        }

        public Builder setConsume(javax.ws.rs.core.MediaType mediaType){
            this.consume = mediaType;
            return this;
        }

        public Builder addProperty(String key,Object value){
            properties.put(key,value);
            return this;
        }

        public Builder authentication(org.glassfish.jersey.client.authentication.HttpAuthenticationFeature feature){
            this.feature = feature;
            return this;
        }

        public CallOptions build(){
            HashMap<String,Object> callOptions = new HashMap<>();
            callOptions.put("parameters",parameters);
            callOptions.put("headers",headers);
            callOptions.put("produce",produce);
            callOptions.put("consume",consume);
            callOptions.put("properties",properties);
            callOptions.put("feature",feature);
            return new CallOptions(callOptions);
        }


    }

}
