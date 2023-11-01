package org.softauto.grpc;

import com.fasterxml.jackson.databind.JsonNode;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.apache.avro.grpc.AvroGrpcServer;
//import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.CallFuture;
import org.softauto.core.ClassType;
import org.softauto.core.ServiceLocator;
import org.softauto.plugin.api.Provider;
import org.softauto.serializer.Serializer;
import org.softauto.serializer.service.Message;
import org.softauto.serializer.service.MessageType;
import org.softauto.serializer.service.SerializerService;

import javax.lang.model.element.Element;
import java.io.IOException;
import java.util.HashMap;

/**
 * grpc server impl
 * this class is singleton
 */
public class RpcProviderImpl  {

    private static RpcProviderImpl rpcProviderImpl = null;
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(RpcProviderImpl.class);

    /** default listener host **/
    String host = "localhost";

    /** default protocol type **/
    String type = "RPC";

    /** default grpc server port **/
    int port = 8085;



    Server server = null;

    /** the interface for this service **/
    Class iface;




    public static RpcProviderImpl getInstance(){
        if(rpcProviderImpl == null){
            rpcProviderImpl =  new RpcProviderImpl();
        }
        return rpcProviderImpl;
    }



    public RpcProviderImpl iface(Class iface) {
        this.iface = iface;
        return this;
    }




    public RpcProviderImpl initialize() throws IOException {
        try {
            server = ServerBuilder.forPort(port)
                    .addService(AvroGrpcServer.createServiceDefinition(SerializerService.class, new SerializerServiceImpl()))
                    //.addService(AvroGrpcServer.createServiceDefinition(SerializerService.Callback.class, new SerializerServiceImpl()))
                    .build();
            server.start();
            //server.getServices();
            logger.info("Grpc Server load successfully on port "+port);
        }catch (Exception e){
            logger.fatal("fail to start Serializer server ", e);
            System.exit(1);
        }
        return this;
    }




    public void register() {
        ServiceLocator.getInstance().register(type,this);
    }

    public void  exec(String name,  ManagedChannel channel, Object[] args, Class[] types, HashMap<String,Object> callOptions){
        Object result = null;
        try {
            doExec(name,  channel,  args,  types,  callOptions);
        }catch (Exception e){
            logger.error("fail exec rpc call "+ name, e);
        }
    }

    public <RespT> void exec(String name, CallFuture<RespT> callback, ManagedChannel channel, Object[] args, Class[] types, HashMap<String,Object> callOptions){
        Object result = null;
        try {
            result = doExec(name,  channel,  args,  types,  callOptions);
        }catch (Exception e){
            logger.error("fail exec rpc call "+ name, e);
        }
        callback.handleResult((RespT) result);
        logger.debug("callback value "+callback.getResult()+" get error "+callback.getError());
    }

    private Object doExec(String name,  ManagedChannel channel, Object[] args, Class[] types, HashMap<String,Object> callOptions){
        Object result = null;
        try {
            logger.debug("exec rpc call "+ name);
            Serializer serializer;
            if(channel != null) {
                serializer = new Serializer().setChannel(channel);
            }else {
                serializer = new Serializer().setHost(host).setPort(port).build();
            }
            ClassType classType = ClassType.fromString(callOptions.get("classType").toString());//Utils.getClassType(name,types);
            MessageType messageType =MessageType.fromString(callOptions.get("messageType").toString()); //Utils.getMessageType(name,types);

                if(callOptions.get("classType")!= null){
                    classType = ClassType.fromString(callOptions.get("classType").toString());
                }
                if(callOptions.get("messageType")!= null){
                    messageType = MessageType.fromString(callOptions.get("messageType").toString());
                }

            Message message = Message.newBuilder().setDescriptor(name).setType(messageType).setArgs((Object[]) args).setTypes(types).addData("classType",classType.name()).build();
            result = serializer.write(message);

        }catch (Exception e){
            logger.error("fail exec rpc call "+ name, e);
        }
        return result;
    }



}
