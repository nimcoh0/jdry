package org.softauto.analyzer.model.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.directivs.argument.Argument;
import org.softauto.analyzer.directivs.callback.CallBack;
import org.softauto.analyzer.directivs.request.Request;
import org.softauto.analyzer.directivs.result.Result;
import org.softauto.analyzer.model.asserts.Assert;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ApiBuilder {

    private static Logger logger = LogManager.getLogger(ApiBuilder.class);

    public static Builder newBuilder() { return new Builder();}

    private Api api;

    public ApiBuilder(Api api){
        this.api = api;
    }

    public Api getApi() {
        return api;
    }

    public static class Builder  {

        protected String protocol;

        protected String classType;

        protected CallBack callback;

        protected String id;

        protected Result result;

        protected LinkedList<Object> childes = new LinkedList();

        protected Request request = new Request();

        protected String method;

        protected String namespce;

        protected String description;

        protected String fullName;


        public String getProtocol(){
            return protocol;
        }

        public Builder setFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder setMethod(String method) {
            this.method = method;
            return this;
        }

        public Builder setNamespce(String namespce) {
            this.namespce = namespce;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setRequest(Request request) {
            this.request = request;
            return this;
        }

        public Builder addArgument(Argument argument){
            this.request.getArguments().add(argument);
            return this;
        }

        public Builder setChildes(List<Object> childes) {
            if(childes != null && childes.size() > 0)
                this.childes.addAll(childes);
            return this;
        }

        public Builder addChilde(String childe) {
            if(childe != null)
                this.childes.add(childe);
            return this;
        }

        public Builder setId(String id ) {
            if(id != null)
                this.id = id;
            return this;
        }

        public Builder setCallback(CallBack callback) {
            this.callback = callback;
            return this;
        }

        public Builder setClassType(String classType) {
            this.classType = classType;
            return this;
        }

        public Builder setProtocol(Object protocol) {
            if(protocol != null) {
                this.protocol = protocol.toString();
            }else {
                this.protocol = "RPC";
            }
            return this;
        }

        public Builder setResult(Result result) {
            this.result = result;
            return this;
        }

        public ApiBuilder build(){
            Api api = new Api();
            try {
                api.setDescription(description);
                api.setId(id);
                api.setChildes(childes);
                api.setMethod(method);
                api.setNamespace(namespce);
                api.setRequest(request);
                api.setResponse(result);
                api.setProtocol(protocol);
                api.setCallback(callback);
                api.setClassType(classType);
                api.setFullName(fullName);
                logger.debug("successfully build Api "+fullName);
            } catch (Exception e) {
                logger.error("fail build Api "+fullName,e);
            }
            return new ApiBuilder(api);
        }
    }
}
