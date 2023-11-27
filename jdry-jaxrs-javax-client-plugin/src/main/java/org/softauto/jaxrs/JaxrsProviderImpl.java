package org.softauto.jaxrs;

import com.fasterxml.jackson.databind.JsonNode;
import io.grpc.ManagedChannel;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.ClassUtils;
import org.softauto.core.*;
import org.softauto.jaxrs.auth.AuthFactory;
import org.softauto.jaxrs.service.*;
import org.softauto.plugin.api.Provider;
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

    /*
    @Override
    public IStepDescriptor buildStepDescriptor(String stepName, Object[] args, Class[] types, HashMap<String, Object> callOptions, String scenarioId, String auth) {
        IStepDescriptor stepDescriptor = AuthFactory.getStepDescriptor(auth);
        return StepDescriptorBuilder.newBuilder().setStepDescriptor(stepDescriptor).setName(stepName).setArgs(args).setTypes(types).setCallOptions(callOptions).setScenarioId(scenarioId).build().getStepDescriptor();
    }





    public <RespT> void exec(IStepDescriptor stepDescriptor,org.apache.avro.ipc.Callback<Object> callback) {
        executor.submit(()-> {
            try {
                CallbackToResponseStreamObserverAdpater observerAdpater = new CallbackToResponseStreamObserverAdpater(callback, null);
                testDefinition = RestService.createTestDefinition(stepDescriptor.getFullMethodName(),
                        stepDescriptor.getArgs(),
                        stepDescriptor.getTypes(),
                        stepDescriptor.getProperties(),
                        stepDescriptor.getScenarioId());
                StepDefinition md = testDefinition.getStep(stepDescriptor.getFullMethodName());
                RespT res = (RespT) md.getCallerHandler().startCall(md.getStepDescriptor(), stepDescriptor.getArgs());
                if (((Response)res).getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                    if(((Response)res).hasEntity()) {
                        Class c = ClassUtils.getClass(stepDescriptor.getProperties().get("response").toString());
                        observerAdpater.onCompleted(((Response)res).readEntity(c));
                    }else {
                        observerAdpater.onCompleted((RespT) res);
                    }
                } else {
                    observerAdpater.onError(new RuntimeException(((Response)res).getStatusInfo().getReasonPhrase()));
                }
                logger.debug("successfully exec jaxrs call  "+  stepDescriptor.getFullMethodName());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
    }

    public <RespT> Object exec(IStepDescriptor stepDescriptor) {
        try {
        testDefinition = RestService.createTestDefinition(stepDescriptor.getFullMethodName(),
                stepDescriptor.getArgs(),
                stepDescriptor.getTypes(),
                stepDescriptor.getProperties(),
                stepDescriptor.getScenarioId());
            StepDefinition md = testDefinition.getStep(stepDescriptor.getFullMethodName());
            RespT res = (RespT)md.getCallerHandler().startCall(md.getStepDescriptor(),stepDescriptor.getArgs());
            if (((Response)res).getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                if(((Response)res).hasEntity()) {
                    Class c = ClassUtils.getClass(stepDescriptor.getProperties().get("response").toString());
                    return ((Response)res).readEntity(c);
                }else {
                    return (RespT) res;
                }
            } else {
                new RuntimeException(((Response)res).getStatusInfo().getReasonPhrase());
            }
            logger.debug("successfully exec jaxrs call  "+  stepDescriptor.getFullMethodName());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }


     */
    @Override
    public <RespT> Object exec(String stepName, ManagedChannel channel, Object[] args, Class[] types, HashMap<String, Object> callOptions,String scenarioId) {
        try {
            //executor.submit(()->{
                try {
                    testDefinition = RestService.createTestDefinition(stepName,args,types,callOptions,scenarioId);
                    StepDefinition md = testDefinition.getStep(stepName);
                    RespT res = (RespT)md.getCallerHandler().startCall(md.getStepDescriptor(),args);
                    if (((Response)res).getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                        if(((Response)res).hasEntity()) {
                            Class c = ClassUtils.getClass(callOptions.get("response").toString());
                            return ((Response)res).readEntity(c);
                        }else {
                            return (RespT) res;
                        }
                    } else {
                        new RuntimeException(((Response)res).getStatusInfo().getReasonPhrase());
                    }


                    logger.debug("successfully exec jaxrs call  "+  stepName);
                } catch (Throwable e) {
                    e.printStackTrace();
                }

            //});
        }catch (Exception e){
            logger.error("exec jaxrs call  fail "+  stepName,e);
        }
        return null;
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
    public <RespT> void exec(String stepName, org.apache.avro.ipc.Callback<RespT> callback, ManagedChannel channel, Object[] args, Class[] types, HashMap<String,Object> callOptions,String scenarioId) {
        try {
            executor.submit(()->{
                try {
                    CallbackToResponseStreamObserverAdpater observerAdpater = new CallbackToResponseStreamObserverAdpater(callback, null);
                    testDefinition = RestService.createTestDefinition(stepName,args,types,callOptions,scenarioId);
                    StepDefinition md = testDefinition.getStep(stepName);
                    //RespT res = (RespT)md.getCallerHandler().startCall(md.getStepDescriptor(),args);
                    RespT res = (RespT)md.getCallerHandler().startCall(md.getStepDescriptor(),args);
                    if (((Response)res).getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                        if(((Response)res).hasEntity()) {
                            Class c = ClassUtils.getClass(callOptions.get("response").toString());
                            observerAdpater.onCompleted(((Response)res).readEntity(c));
                            //observerAdpater.onCompleted(((Response)res).getEntity());
                        }else {
                            observerAdpater.onCompleted((RespT) res);
                        }
                    } else {
                        observerAdpater.onError(new RuntimeException(((Response)res).getStatusInfo().getReasonPhrase()));
                    }


                    logger.debug("successfully exec jaxrs call  "+  stepName);
                } catch (Throwable e) {
                    e.printStackTrace();
                }

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
