package org.softauto.listener.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.apache.avro.grpc.AvroGrpcServer;
import org.apache.avro.ipc.CallFuture;
import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.core.ServiceLocator;
import org.softauto.serializer.service.SerializerService;

import java.util.HashMap;


/**
 * listener server impl class.
 *
 */
public class ListenerServerProviderImpl  {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ListenerServerProviderImpl.class);
    private static ListenerServerProviderImpl listenerProviderImpl = null;
    ObjectMapper objectMapper;


    /**
     * this provider name
     */
    String type = "LISTENER-SERVER";

    /**
     * the schema interface class
     */
    Class iface;

    /**
     * listener server
     */
    Server server = null;




    private ListenerServerProviderImpl(){
        objectMapper = new ObjectMapper(new YAMLFactory());
    }


    public static ListenerServerProviderImpl getInstance(){
        if(listenerProviderImpl == null){
            listenerProviderImpl =  new ListenerServerProviderImpl();
         }
        return listenerProviderImpl;
    }







    public ListenerServerProviderImpl initialize()  {
        try {
            server = ServerBuilder.forPort(Integer.valueOf(Configuration.get(Context.LISTENER_PORT)))
                    .addService(AvroGrpcServer.createServiceDefinition(SerializerService.class, new ListenerServiceImpl()))
                    .build();
            server.start();
            logger.info("listener server load successfully on port "+ Configuration.get(Context.LISTENER_PORT));
        }catch (Exception e) {
            logger.error("start Listener server fail  ", e);
        }

        return this;
    }



    public void register() {
        ServiceLocator.getInstance().register("LISTENER-SERVER",server);

    }


    public void shutdown(){
       server.shutdown();
    }


    public String getType() {
        return type;
    }


    public ListenerServerProviderImpl iface(Class iface) {
        this.iface = iface;
        return this;
    }


    public <RespT> void exec(String name, CallFuture<RespT> callback, ManagedChannel channel, Object[] args, Class[] types, HashMap<String, Object> callOptions) {

    }






}
