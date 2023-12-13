package org.softauto.jaxrs;

import com.fasterxml.jackson.databind.JsonNode;
import io.grpc.ManagedChannel;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.ClassUtils;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.core.*;
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

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");
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


    @Override
    public <RespT> Object exec(String stepName, ManagedChannel channel, Object[] args, Class[] types, HashMap<String, Object> callOptions,String scenarioId) {
        try {
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


                    logger.debug(JDRY,"successfully exec jaxrs call  "+  stepName);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
        }catch (Exception e){
            logger.error(JDRY,"exec jaxrs call  fail "+  stepName,e);
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
                    RespT res = (RespT)md.getCallerHandler().startCall(md.getStepDescriptor(),args);
                    if (((Response)res).getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                        if(((Response)res).hasEntity()) {
                            Class c = ClassUtils.getClass(callOptions.get("response").toString());
                            observerAdpater.onCompleted(((Response)res).readEntity(c));
                        }else {
                            observerAdpater.onCompleted((RespT) res);
                        }
                    } else {
                        observerAdpater.onError(new RuntimeException(((Response)res).getStatusInfo().getReasonPhrase()));
                    }
                    logger.debug(JDRY,"successfully exec jaxrs call  "+  stepName);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            });
        }catch (Exception e){
            logger.error(JDRY,"exec jaxrs call  fail "+  stepName,e);
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
        return this;
    }


    @Override
    public void register() {
        ServiceLocator.getInstance().register(type,this);
    }





}
