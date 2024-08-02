package org.softauto.spring.web.analyzer.util;


import org.softauto.analyzer.core.system.plugin.api.Provider;
import org.softauto.analyzer.model.genericItem.GenericItem;

import java.util.ArrayList;
import java.util.HashMap;

public class CallOptionBuilder {

    public static Builder newBuilder() {
        return new Builder();
    }

    HashMap<String,Object> callOption;

    public CallOptionBuilder(HashMap<String,Object> callOption){
        this.callOption = callOption;
    }

    public HashMap<String, Object> getCallOption() {
        return callOption;
    }

    public static class Builder<T> {

        private Object path;

        private GenericItem tree;

        private String produce;

        private String consume;

        private String method;

        private String response;

        private String[] argumentsNames;

        private Provider provider;

        public Builder setProvider(Provider provider) {
            this.provider = provider;
            return this;
        }

        private String[] buildArgumentsNames(){
            String[] argumentsNames = new String[tree.getRequest().getArguments().size()];
            for(int i=0;i< tree.getRequest().getArguments().size() ;i++ ){
                argumentsNames[i] = tree.getRequest().getArguments().get(i).getName();
            }
            return argumentsNames;
        }

        private String findMethod(){
            if (tree.getAnnotations().containsKey("org.springframework.web.bind.annotation.PostMapping;")) {
                return "POST" ;
            }
            if (tree.getAnnotations().containsKey("org.springframework.web.bind.annotation.DeleteMapping")) {
                return "DELETE" ;
            }
            if (tree.getAnnotations().containsKey("org.springframework.web.bind.annotation.GetMapping")) {
                return "GET" ;
            }
            if (tree.getAnnotations().containsKey("org.springframework.web.bind.annotation.PutMapping")) {
                return "PUT" ;
            }
            return null;
        }

        public Builder setPath(Object path) {
            if(((ArrayList)path).size() > 1 ){
                String path1 = "";
                for(Object p : (ArrayList)path){
                    path1 = path1 +p;
                }
                this.path = path1;
            }else if(((ArrayList)path).size() == 1 ){
                this.path = ((ArrayList)path).get(0);
            }
            return this;
        }

        public Builder setTree(GenericItem tree) {
            this.tree = tree;
            return this;
        }

        public Builder setProduce(String produce) {
            this.produce = produce;
            return this;
        }

        public Builder setConsume(String consume) {
            this.consume = consume;
            return this;
        }

        /*
        private String buildResponse(){
            String schema = null;
            if(tree.getReturnType() != null && !tree.getReturnType().equals("void")) {
                if (Utils.isPrimitive(tree.getReturnType()) && Configuration.has("plugin_jaxrs_default_api_primitive_return_type")) {
                    schema = Configuration.get("plugin_jaxrs_default_api_primitive_return_type").asString();
                } else if (Configuration.has("plugin_jaxrs_default_api_return_type")) {
                    schema = Configuration.get("plugin_jaxrs_default_api_return_type").asString();
                } else {
                    schema = tree.getReturnType();
                }
                return Espl.getInstance().evaluate(schema).toString();
            }
            return null;
            //return Espl.getInstance().evaluate(schema).toString();

        }


         */

        public CallOptionBuilder build(){
            method = findMethod();
            //response = buildResponse();
            argumentsNames = buildArgumentsNames();
            HashMap<String,Object> callOption = new HashMap<>();
            callOption.put("method",method);
            callOption.put("response",  tree.getReturnType());
            callOption.put("path",path);
            callOption.put("produces",produce);
            callOption.put("consumes",consume);
            callOption.put("argumentsNames",argumentsNames);
            return new CallOptionBuilder(callOption);
        }
    }
}
