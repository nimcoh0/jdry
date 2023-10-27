package org.softauto.analyzer.model.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.directivs.argument.Argument;
import org.softauto.analyzer.directivs.request.Request;
import org.softauto.analyzer.directivs.result.Result;


public class ListenerBuilder {

    private static Logger logger = LogManager.getLogger(ListenerBuilder.class);

    public static Builder newBuilder() { return new Builder();}

    private Listener listener;

    public ListenerBuilder(Listener listener){
        this.listener = listener;
    }

    public Listener getListener() {
        return listener;
    }

    public static class Builder  {

        protected Object expression;

        protected Result result;

        protected String id;

        protected long time;

        protected String discoveryId;

        protected String callback;

        protected Request request = new Request();

        protected String method;

        protected String namespce;

        protected String description;

        protected String fullName;

        public Builder setExpression(Object expression) {
            this.expression = expression;
            return this;
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

        public Builder setResult(Result result) {
            this.result = result;
            return this;
        }

        public Builder setDiscoveryId(String discoveryId) {
            this.discoveryId = discoveryId;
            return this;
        }

        public Builder setTime(long time) {
            this.time = time;
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setCallback(String callback) {
            this.callback = callback;
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

        public ListenerBuilder build(){
            Listener listener = new Listener();
            try {
                listener.setMethod(method);
                listener.setDescription(description);
                listener.setRequest(request);
                listener.setResponse(result);
                listener.setNamespce(namespce);
                listener.setId(id);
                listener.setFullName(fullName);
                listener.setCallback(callback);
                listener.setExpression(expression);

            } catch (Exception e) {
                logger.error("fail build listener "+ fullName,e);
            }
            return new ListenerBuilder(listener);
        }
    }
}
