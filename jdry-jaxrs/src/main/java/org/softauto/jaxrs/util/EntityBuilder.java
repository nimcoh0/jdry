package org.softauto.jaxrs.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.media.multipart.*;
import org.softauto.core.Utils;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityBuilder {

    public static Builder newBuilder (){
        return new Builder();
    }

    Entity<?> entity = null;

    public EntityBuilder(Entity<?> entity){
        this.entity = entity;
    }

    public Entity<?> getEntity() {
        return entity;
    }

    public static class Builder{

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
                //if(args != null && args.length >0) {
                 //   for (int i = 0; i < args.length; i++) {
                 //       Threadlocal.getInstance().add(argsNames.get(i), args[i]);
                 //   }
                //}


                if (produce != null && (produce.toString().equals(MediaType.MULTIPART_FORM_DATA_TYPE) || produce.toString().equals(MediaType.MULTIPART_FORM_DATA))) {

                    Form form = new Form();
                    for (int i = 0; i < args.length; i++) {
                        String json = null;
                        if(args[i].getClass().isPrimitive() || args[i].getClass().getName().equals("java.lang.String")){
                            json = String.valueOf(args[i]);
                        }else {
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
                        if(args[i].getClass().isPrimitive() || args[i].getClass().getName().equals("java.lang.String")){
                            json = String.valueOf(args[i]);
                        }else {
                            json = new ObjectMapper().writeValueAsString(args[i]);
                        }
                        form.param(argsNames.get(i), json);
                    }
                    entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED);
                }
                if (produce != null &&  produce.toString().equals(MediaType.APPLICATION_JSON)) {
                    //int counter = 0;
                    if (this.callOptions.containsKey("argumentsRequestType")) {
                        HashMap<String, Object> argumentsRequestTypes = (HashMap<String, Object>) callOptions.get("argumentsRequestType");
                        for (Map.Entry map : argumentsRequestTypes.entrySet()) {
                           // Map.Entry o = (Map.Entry) argumentsRequestTypes.entrySet().toArray()[i];
                            if (map.getKey().equals("RequestBody")) {
                                if(((ArrayList)map.getValue()).size() == 1){
                                  String index =   ((Map)((ArrayList)map.getValue()).get(0)).get("index").toString();
                                  Object o = args[Integer.valueOf(index)];
                                  entity = Entity.entity(o, MediaType.APPLICATION_JSON);
                                  //return new EntityBuilder(entity);
                                }else {
                                    FormDataMultiPart multipart = new FormDataMultiPart();
                                    for(int i=0;i<((ArrayList)map.getValue()).size();i++){
                                            String index = ((Map) ((ArrayList) map.getValue()).get(i)).get("index").toString();
                                            Object o = args[Integer.valueOf(index)];
                                            multipart.field(argsNames.get(i), args[i], MediaType.APPLICATION_JSON_TYPE);
                                    }
                                    entity = Entity.entity(multipart, multipart.getMediaType());
                                    //return new EntityBuilder(entity);
                                }

                            }
                        }
                    }
                    /*
                    HashMap<String, Object> argumentsRequestTypes = (HashMap<String, Object>) callOptions.get("argumentsRequestType");
                    FormDataMultiPart multipart = new FormDataMultiPart();
                    for (int i = 0; i < args.length; i++) {
                        Map.Entry o = (Map.Entry) argumentsRequestTypes.entrySet().toArray()[i];
                        if (o.getKey().equals("RequestBody")) {
                            if (counter == 1) {
                                entity = Entity.entity(args[i], MediaType.APPLICATION_JSON);
                                return new EntityBuilder(entity);
                            } else {

                                //if (this.callOptions.containsKey("argumentsRequestType")) {
                                    //HashMap<String, Object> argumentsRequestTypes = (HashMap<String, Object>) callOptions.get("argumentsRequestType");

                                    //MultiPart multiPartEntity = new MultiPart().bodyPart(new BodyPart().entity("hello"));

                                    //FormDataMultiPart multipart = new FormDataMultiPart();
                                    //for(int i=0;i<args.length;i++){
                                    // if(args[i].getClass().getTypeName().contains("Integer")){
                                    //   FormDataBodyPart p = new FormDataBodyPart(argsNames.get(i), args[i].toString());
                                    //  multipart.setEntity(argsNames.get(i), args[i].toString()).bodyPart(p);
                                    //}else {
                                    //FormDataBodyPart p = new FormDataBodyPart(argsNames.get(i), args[i], MediaType.APPLICATION_JSON_TYPE);
                                    //multiPartEntity.bodyPart(p);
                                    //}
                                    //}
                                    //  multiPartEntity.bodyPart(new BodyPart().entity(args[i]),MediaType.APPLICATION_JSON_TYPE);
                                    //}

                                    //FormDataMultiPart multipart = new FormDataMultiPart();

                                    //for (int i = 0; i < args.length; i++) {
                                        //Map.Entry o = (Map.Entry) argumentsRequestTypes.entrySet().toArray()[i];
                                        //if (o.getKey().equals("RequestBody")) {
                                            //FormDataBodyPart p = new FormDataBodyPart(argsNames.get(i), args[i],MediaType.APPLICATION_JSON_TYPE);
                                            multipart.field(argsNames.get(i), args[i], MediaType.APPLICATION_JSON_TYPE);
                                        //}
                                        //multipart.bodyPart(p);
                                    //}

                                }
                            entity = Entity.entity(multipart, multipart.getMediaType());
                                //entity = Entity.entity(multiPartEntity, multiPartEntity.getMediaType());

                            }
                          }
                    */

                }


            }catch (Exception e){
                e.printStackTrace();
            }
            return new EntityBuilder(entity);
        }

    }
}
