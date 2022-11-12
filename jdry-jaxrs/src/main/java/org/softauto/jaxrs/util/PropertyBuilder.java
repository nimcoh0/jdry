package org.softauto.jaxrs.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.util.Map;

public class PropertyBuilder<T> {

    public static Builder newBuilder() { return new Builder();}

    T prop;

    public PropertyBuilder(T prop){
        this.prop = prop;
    }

    public T get(){
        return (T)prop;
    }

    public <T> PropertyBuilder get(Handler<T> resultHandler)  {
        try {
            resultHandler.handle((T)prop);
        }catch (Exception e){
            e.printStackTrace();
        }
        return this;
    }


    public static class Builder<T> {

        Map<String, Object> map;

        String path;

        String root;


        public Builder setMap(Map<String, Object> map) {
            this.map = map;
            return this;
        }

        public Builder setPath(String path) {
            root = path.substring(0,path.indexOf("/"));
            this.path = path.replace(root,"");
            return this;
        }

        private Object build(ArrayNode list){
            if(list.size() > 0){
             return (T)list;
            }
            return null;
        }

        private Object build(JsonNode node){
            if(!node.isNull() && node.size() > 0){
               return (T)node;
            }
            return null;
        }

        private Object build(TextNode string){
            if(!string.asText().isEmpty()){
               return (T)string.asText();
            }
            return null;
        }

        public PropertyBuilder build(){
            Object node = null;
            JsonNode jsonNode = org.apache.avro.util.internal.JacksonUtils.toJsonNode(map);
            if(jsonNode.has(root) && !jsonNode.get(root).at(path).isNull()){
                Object o = jsonNode.get(root).at(path);
                if(o instanceof ArrayNode){
                    node =  build((ArrayNode)o);
                }else
                if(o instanceof TextNode  ){
                    node =  build((TextNode)o);
                }else
                if(o instanceof ObjectNode){
                    node =  build((ObjectNode)o);
                }
             }

            return new PropertyBuilder(node);
        }



    }
}
