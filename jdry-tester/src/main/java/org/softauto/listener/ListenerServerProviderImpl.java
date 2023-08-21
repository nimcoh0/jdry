package org.softauto.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.apache.avro.grpc.AvroGrpcServer;
import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.core.ServiceLocator;
import org.softauto.serializer.service.SerializerService;

public class ListenerServerProviderImpl  {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ListenerServerProviderImpl.class);

    private static ListenerServerProviderImpl listenerProviderImpl = null;

    ObjectMapper objectMapper;

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
            server = ServerBuilder.forPort(Configuration.get(Context.LISTENER_PORT).asInteger())
                    .addService(AvroGrpcServer.createServiceDefinition(SerializerService.class, new ListenerServiceImpl()))
                    .build();
            server.start();
            logger.info("listener server load successfully on port "+ Configuration.get(Context.LISTENER_PORT).asString());
        }catch (Exception e) {
            logger.error("start Listener server fail  ", e);
        }

        return this;
    }

    public void shutdown(){
        server.shutdown();
    }

    public void register() {
        ServiceLocator.getInstance().register("LISTENER-SERVER",server);

    }
}
