package org.softauto.jaxrs.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public  class EntityBuilder<T> {

    public static Builder newBuilder (){
        return new Builder();
    }

    protected T entity = null;



    public EntityBuilder(T entity){
        this.entity = entity;
    }

    public T getEntity(){
        return entity;
    }



    public static class Builder<T>{

        Entity<?> entity = null;

        MediaType produce;

        Object[] args;

        List<String> argsNames;

        protected HashMap<String,Object> callOptions;

        public Builder setCallOptions(HashMap<String, Object> callOptions) {
            this.callOptions = callOptions;
            return this;
        }

        public Builder setArgsNames(List<String> argsNames) {
            this.argsNames = argsNames;
            return this;
        }

        public Builder setProduce(MediaType produce) {
            this.produce = produce;
            return this;
        }

        public Builder setArgs(Object[] args) {
            this.args = args;
            return this;
        }

        public EntityBuilder build(){
            try{

                if (produce != null && (produce.toString().equals(MediaType.MULTIPART_FORM_DATA_TYPE) || produce.toString().equals(MediaType.MULTIPART_FORM_DATA))) {

                    Form form = new Form();
                    for (int i = 0; i < args.length; i++) {
                        String json = null;
                        if (args[i].getClass().isPrimitive() || args[i].getClass().getName().equals("java.lang.String")) {
                            json = String.valueOf(args[i]);
                        } else {
                            json = new ObjectMapper().writeValueAsString(args[i]);
                        }
                        form.param(argsNames.get(i), json);
                    }

                    entity = Entity.entity(form, MediaType.MULTIPART_FORM_DATA_TYPE);
                }
                if (produce != null && produce.toString().equals(MediaType.APPLICATION_FORM_URLENCODED)) {
                    Form form = new Form();
                    for (int i = 0; i < args.length; i++) {
                        String json = null;
                        if (args[i].getClass().isPrimitive() || args[i].getClass().getName().equals("java.lang.String")) {
                            json = String.valueOf(args[i]);
                        } else {
                            json = new ObjectMapper().writeValueAsString(args[i]);
                        }
                        form.param(argsNames.get(i), json);
                    }
                    entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED);
                }
                if (produce != null && produce.toString().equals(MediaType.APPLICATION_JSON)) {
                    //int counter = 0;
                    if (this.callOptions.containsKey("argumentsRequestType")) {
                        HashMap<String, Object> argumentsRequestTypes = (HashMap<String, Object>) callOptions.get("argumentsRequestType");
                        if (argumentsRequestTypes != null && argumentsRequestTypes.size() > 0) {
                            for (Map.Entry map : argumentsRequestTypes.entrySet()) {
                                // Map.Entry o = (Map.Entry) argumentsRequestTypes.entrySet().toArray()[i];
                                if (map.getKey().equals("FormParam") || map.getKey().equals("RequestBody")) {
                                    if (((ArrayList) map.getValue()).size() == 1) {
                                        String index = ((Map) ((ArrayList) map.getValue()).get(0)).get("index").toString();
                                        Object o = args[Integer.valueOf(index)];
                                        entity = Entity.entity(o, MediaType.APPLICATION_JSON);
                                        //return new EntityBuilder(entity);
                                    } else {
                                        Map<String, Object> request = new HashMap<>();
                                        for(int i=0;i<((ArrayList)map.getValue()).size();i++){
                                            String index = ((Map) ((ArrayList) map.getValue()).get(i)).get("index").toString();
                                            Object o = args[Integer.valueOf(index)];
                                            request.put(argsNames.get(i), args[i]);
                                        }
                                        entity = Entity.entity(request,MediaType.APPLICATION_JSON);

                                    }

                                }
                            }
                        }
                    } else {
                        if (args.length == 1) {
                            entity = Entity.entity(args[0], MediaType.APPLICATION_JSON);
                        } else {
                            FormDataMultiPart multipart = new FormDataMultiPart();
                            for (int i = 0; i < args.length; i++) {
                                multipart.field(argsNames.get(i), args[i], MediaType.APPLICATION_JSON_TYPE);
                            }
                            entity = Entity.entity(multipart, multipart.getMediaType());
                        }


                    }
                }


            }catch (Exception e){
                e.printStackTrace();
            }
            return new EntityBuilder(entity);

        }

    }
}
