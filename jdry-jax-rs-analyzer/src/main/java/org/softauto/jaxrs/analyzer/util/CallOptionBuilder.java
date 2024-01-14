package org.softauto.jaxrs.analyzer.util;


import org.softauto.analyzer.core.system.plugin.api.Provider;
import org.softauto.analyzer.model.genericItem.GenericItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        private Provider provider;

        private Object path;

        private GenericItem tree;

        private String produce;

        private String consume;

        private String method;

        private String response;

        private String[] argumentsNames;

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
            for(Map.Entry map : tree.getAnnotations().entrySet()) {
                if (map.getKey().toString().contains(".ws.rs.POST")) {
                    return "POST";
                }
            }
            for(Map.Entry map : tree.getAnnotations().entrySet()) {
                if (map.getKey().toString().contains(".ws.rs.DELETE")) {
                    return "DELETE";
                }
            }
            for(Map.Entry map : tree.getAnnotations().entrySet()) {
                if (map.getKey().toString().contains(".ws.rs.GET")) {
                    return "GET";
                }
            }
            for(Map.Entry map : tree.getAnnotations().entrySet()) {
                if (map.getKey().toString().contains(".ws.rs.PUT")) {
                    return "PUT";
                }
            }
            return null;
        }

        public Builder setPath(Object path) {
            if(((ArrayList)path).size() > 1 ){
                this.path = path;
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


        public CallOptionBuilder build(){
            method = findMethod();
            argumentsNames = buildArgumentsNames();
            HashMap<String,Object> callOption = new HashMap<>();
            callOption.put("method",method);
            callOption.put("response", tree.getUnboxReturnType() != null ? tree.getUnboxReturnType() : tree.getReturnType());
            callOption.put("path",path);
            callOption.put("produces",produce);
            callOption.put("consumes",consume);
            callOption.put("argumentsNames",argumentsNames);
            return new CallOptionBuilder(callOption);
        }
    }
}
