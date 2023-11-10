package org.softauto.analyzer.model.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.directivs.request.Request;
import org.softauto.analyzer.directivs.result.Result;

import java.util.HashMap;


public class DataBuilder {

    private static Logger logger = LogManager.getLogger(DataBuilder.class);

    public static Builder newBuilder() { return new Builder();}

    private Data data;

    public DataBuilder(Data data){
       this.data = data;
    }

    public Data getData() {
        return data;
    }

    public static class Builder {

        protected Request request = new Request();

        protected Result result;

        protected String clazz;

        protected String plugin;

        protected String method;

        protected long thread;

        protected long time;

        protected int id;

        protected HashMap<String, Object> pluginData = new HashMap<>();

        HashMap<String, Object> threadLocal = new HashMap<>();

        public Builder setThreadLocal(HashMap<String, Object> threadLocal) {
            this.threadLocal = threadLocal;
            return this;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setRequest(Request request) {
            this.request = request;
            return this;
        }

        public Builder setResult(Result result) {
            this.result = result;
            return this;
        }

        public Builder setMethod(String method) {
            this.method = method;
            return this;
        }

        public Builder setPluginData(HashMap<String, Object> pluginData) {
            this.pluginData = pluginData;
            return this;
        }

        public Builder setTime(Object time) {
            if(time != null && time instanceof Long){
                this.time = ((Long) time).longValue();
            }
            return this;
        }

        public Builder setThread(long thread) {
            this.thread = thread;
            return this;
        }

       public Builder setClazz(String clazz) {
            this.clazz = clazz;
            return this;
        }

        public Builder setPlugin(Object plugin) {
            if(plugin != null)
                this.plugin = plugin.toString();
            return this;
        }

        public DataBuilder build(){
            Data data = new Data();
            try {
                data.setMethod(method);
                data.setPlugin(plugin);
                data.setClazz(clazz);
                data.setThread(thread);
                data.setTime(time);
                data.setRequest(request);
                data.setResponse(result);
                data.setPluginData(pluginData);
                data.setId(id);

            } catch (Exception e) {
                logger.error("fail build data "+ method,e);
            }
            return  new DataBuilder(data);
        }
    }
}
