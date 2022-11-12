package org.softauto.jaxrs.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import java.util.List;

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
                if (produce != null && produce.toString().equals(MediaType.MULTIPART_FORM_DATA_TYPE)) {
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
                    if(args.length == 1){
                        entity = Entity.entity(args[0],MediaType.APPLICATION_JSON);
                    }else if(args.length > 1){
                        FormDataMultiPart multipart = new FormDataMultiPart();
                        for(int i=0;i<args.length;i++){
                            multipart.field(argsNames.get(i),args[i],MediaType.APPLICATION_JSON_TYPE);
                        }
                        entity = Entity.entity(multipart,MediaType.APPLICATION_JSON_TYPE);
                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return new EntityBuilder(entity);
        }

    }
}
