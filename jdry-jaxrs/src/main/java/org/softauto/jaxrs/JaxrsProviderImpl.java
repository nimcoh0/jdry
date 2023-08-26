package org.softauto.jaxrs;

import com.fasterxml.jackson.databind.JsonNode;
import io.grpc.ManagedChannel;
import org.apache.avro.ipc.CallFuture;
import org.softauto.core.CallbackToResponseStreamObserverAdpater;
import org.softauto.core.Configuration;
import org.softauto.core.ServiceLocator;
import org.softauto.jaxrs.service.RestService;
import org.softauto.jaxrs.service.StepDefinition;
import org.softauto.jaxrs.service.TestDefinition;
import org.softauto.plugin.ProviderManager;
import org.softauto.plugin.api.Provider;
import org.softauto.plugin.spi.PluginProvider;
import javax.lang.model.element.Element;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * jaxrs plugin Provider Impl
 */
public class JaxrsProviderImpl implements Provider {




    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(JaxrsProviderImpl.class);
    private static JaxrsProviderImpl jaxrsProviderImpl = null;
    Class iface;
    TestDefinition testDefinition;
    String type = "JAXRS";




    private ExecutorService executor = Executors.newSingleThreadExecutor();


    @Override
    public JaxrsProviderImpl iface(Class iface) {
        this.iface = iface;
        return this;
    }



    public static JaxrsProviderImpl getInstance(){
        if(jaxrsProviderImpl == null){
            jaxrsProviderImpl =  new JaxrsProviderImpl();
        }
        return jaxrsProviderImpl;
    }


    @Override
    public JsonNode parser(Element element) {
       // return  new MessageHandler().parser(element);
        return null;
    }

    @Override
    public <RespT> void exec(String stepName, CallFuture<RespT> callback, ManagedChannel channel, Object[] args, Class[] types, HashMap<String,Object> callOptions) {
        try {
            executor.submit(()->{
                CallbackToResponseStreamObserverAdpater observerAdpater = new CallbackToResponseStreamObserverAdpater(callback, null);
                testDefinition = RestService.createTestDefinition(stepName,args,types,callOptions);
                StepDefinition md = testDefinition.getStep(stepName);
                //RespT res = (RespT)md.getCallerHandler().startCall(md.getStepDescriptor(),args);
                RespT res = (RespT)md.getCallerHandler().startCall(md.getStepDescriptor(),args);
                if (res != null) {
                    observerAdpater.onCompleted((RespT)res);
                } else {
                    observerAdpater.onError(new RuntimeException("Stream got cancelled"));
                }


                logger.debug("successfully exec jaxrs call  "+  stepName);

            });
        }catch (Exception e){
            logger.error("exec jaxrs call  fail "+  stepName,e);
        }
    }




    @Override
    public void shutdown() {

    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Provider initialize() throws IOException {
        try {

           // PluginProvider pluginProvider = ProviderManager.provider(Configuration.get("jaxrs").asMap().get("plugin").toString());
           // Provider provider = pluginProvider.create().initialize();


        }catch (Exception e){
            e.printStackTrace();
        }
        return this;
    }


    @Override
    public void register() {
        ServiceLocator.getInstance().register(type,this);
    }





}
