package org.softauto.grpc.client;


import com.fasterxml.jackson.databind.JsonNode;
import io.grpc.ManagedChannel;
import org.softauto.core.ClassType;
import org.softauto.core.ServiceLocator;
import org.softauto.plugin.api.Provider;
import org.softauto.serializer.Serializer;
import org.softauto.serializer.service.Message;
import org.softauto.serializer.service.MessageType;
import javax.lang.model.element.Element;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * grpc server impl
 * this class is singleton
 */
public class RpcProviderImpl implements Provider {

    private static RpcProviderImpl rpcProviderImpl = null;
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(RpcProviderImpl.class);

    private ExecutorService executor = Executors.newSingleThreadExecutor();


    /** default listener host **/
    String host = "localhost";

    /** default protocol type **/
    String type = "RPC";


    /** default grpc server port **/
    int port = 8085;


    public static RpcProviderImpl getInstance(){
        if(rpcProviderImpl == null){
            rpcProviderImpl =  new RpcProviderImpl();
        }
        return rpcProviderImpl;
    }


    @Override
    public Provider initialize() throws IOException {
        return this;
    }

    public void register() {
        ServiceLocator.getInstance().register(type,this);
    }

    @Override
    public void shutdown() {

    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public JsonNode parser(Element element) {
        return null;
    }

    @Override
    public Provider iface(Class iface) {
        return null;
    }



    public <RespT> void exec(String name, org.apache.avro.ipc.Callback<RespT> callback, ManagedChannel channel, Object[] args, Class[] types, HashMap<String,Object> callOptions){
        Object result = null;
        try {
            logger.debug("exec rpc call "+ name);
            Serializer serializer;
            if(channel != null) {
                serializer = new Serializer().setChannel(channel);
            }else {
                serializer = new Serializer().setHost(host).setPort(port).build();
            }
            ClassType classType = ClassType.NONE;
            MessageType messageType = MessageType.METHOD;
            if(callOptions != null  && callOptions.containsKey("classType")) {
                classType = ClassType.fromString(callOptions.get("classType").toString());
            }
            if(callOptions != null  && callOptions.containsKey("messageType")) {
                messageType = MessageType.fromString(callOptions.get("messageType").toString());
            }

            Message message = Message.newBuilder().setDescriptor(name).setType(messageType).setArgs((Object[]) args).setTypes(types).addData("callOption",callOptions).build();
            serializer.write(message,callback);

        }catch (Exception e){
            logger.error("fail exec rpc call "+ name, e);
        }
    }


    public <RespT> Object exec(String name, ManagedChannel channel, Object[] args, Class[] types, HashMap<String,Object> callOptions){
        Object result = null;
        try {
            logger.debug("exec rpc call "+ name);
            Serializer serializer;
            if(channel != null) {
                serializer = new Serializer().setChannel(channel);
            }else {
                serializer = new Serializer().setHost(host).setPort(port).build();
            }
            ClassType classType = ClassType.NONE;
            MessageType messageType = MessageType.METHOD;
            if(callOptions != null  && callOptions.containsKey("classType")) {
                classType = ClassType.fromString(callOptions.get("classType").toString());
            }
            if(callOptions != null  && callOptions.containsKey("messageType")) {
                messageType = MessageType.fromString(callOptions.get("messageType").toString());
            }

            Message message = Message.newBuilder().setDescriptor(name).setType(messageType).setArgs((Object[]) args).setTypes(types).addData("callOption",callOptions).build();
            result = serializer.write(message);

        }catch (Exception e){
            logger.error("fail exec rpc call "+ name, e);
        }
        return result;
    }

}
