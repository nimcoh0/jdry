package org.softauto.listener.manager;


import io.grpc.ManagedChannel;
import org.apache.avro.ipc.CallFuture;
import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.core.ServiceLocator;
import org.softauto.listener.impl.*;
import java.io.IOException;
import java.util.HashMap;

public class ListenerClientProviderImpl  {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ListenerClientProviderImpl.class);
    private static ListenerClientProviderImpl listenerClientProviderImpl = null;


    String type = "LISTENER-MANAGER";

    public static ListenerClientProviderImpl getInstance(){
        if(listenerClientProviderImpl == null){
            listenerClientProviderImpl =  new ListenerClientProviderImpl();
        }
        return listenerClientProviderImpl;
    }


    public ListenerClientProviderImpl initialize() throws IOException {
        try {
            HashMap<String, Object> hm = (HashMap<String, Object>)Configuration.get(Context.LISTENERS);
            //Class iface = Utils.getRemoteOrLocalClass(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText(), Configuration.get(Context.LISTENER_SERVICE_NAME).asText(), Configuration.get(Context.TEST_MACHINE).asText());
            if(hm != null){
                Listener.addSchema(hm);
            }
            ListenerServiceImpl listenerServiceImpl = new ListenerServiceImpl();
            Listener.init(listenerServiceImpl);
            logger.info("successfully load listener manager");
        }catch (Exception e){
            logger.error("fail to load listener manager",e);
        }
        return this;
    }



    public void register() {
        ServiceLocator.getInstance().register("LISTENER-MANAGER",this);
    }





    public String getType() {
        return type;
    }





    public <RespT> void exec(String name, CallFuture<RespT> callback, ManagedChannel channel, Object[] args, Class[] types, HashMap<String, Object> callOptions) {

    }







}
