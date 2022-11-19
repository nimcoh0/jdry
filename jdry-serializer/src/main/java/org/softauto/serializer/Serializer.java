package org.softauto.serializer;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.*;
import org.apache.avro.grpc.AvroGrpcClient;
import org.apache.avro.ipc.CallFuture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.serializer.service.Message;
import org.softauto.serializer.service.SerializerService;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;



public class Serializer {

    private static final Logger logger = LogManager.getLogger(Serializer.class);
    String host;
    int port;
    ManagedChannel channel = null;
    SerializerService clientOneWay = null;
    SerializerService.Callback clientTwoWay = null;
    ConnectivityState connectivityState = null;

    public String getHost() {
        return host;
    }

    public Serializer setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public Serializer setPort(int port) {
        this.port = port;
        return this;
    }

    public ManagedChannel getChannel() {
        return channel;
    }

    public Serializer setChannel(ManagedChannel channel) {
        this.channel = channel;
        connectivityState = this.channel.getState(true);
        this.clientOneWay = (SerializerService)AvroGrpcClient.create(this.channel, SerializerService.class);
        return this;
    }

    public Serializer build(){
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        connectivityState = channel.getState(true);
        clientOneWay = AvroGrpcClient.create(channel, SerializerService.class);
        clientTwoWay = AvroGrpcClient.create(channel, SerializerService.Callback.class);
        logger.debug("channel build using host "+ host + " and port "+ port);
        return this;
    }

    public <T> T write(Message message) throws Exception {
        Object result = null;
        try {
            if(connectivityState.equals(ConnectivityState.READY) || connectivityState.equals(ConnectivityState.IDLE)) {
                byte[] m = new ObjectMapper().writeValueAsBytes(message);
                ByteBuffer byteBuffer = ByteBuffer.wrap(m);
                ByteBuffer res = (ByteBuffer) clientOneWay.execute(byteBuffer);
                String newContent = new String(res.array(), StandardCharsets.UTF_8);
                result =  new ObjectMapper().readValue(newContent,Object.class);
                logger.debug("successfully execute message " + message.toJson());
            }
        }catch (Exception e){
            logger.error("fail execute sync message "+ message.toJson(),e);
        }finally {
            channel.shutdown();
        }
        logger.debug("result "+(T)result);
        return (T)result;
    }


    public <T> void write(Message message,CallFuture<Object> callback) throws Exception {
        Object result = null;
        try {
            if(connectivityState.equals(ConnectivityState.READY) || connectivityState.equals(ConnectivityState.IDLE)) {
                byte[] m = new ObjectMapper().writeValueAsBytes(message);
                ByteBuffer byteBuffer = ByteBuffer.wrap(m);

                clientTwoWay.execute(byteBuffer,callback);
                String newContent = new String(((ByteBuffer)callback.getResult()).array(), StandardCharsets.UTF_8);
                result =  new ObjectMapper().readValue(newContent,Object.class);
                callback.handleResult((T)result);
                logger.debug("successfully execute message " + message.toJson());
            }
        }catch (Exception e){
            logger.error("fail execute sync message "+ message.toJson(),e);
        }finally {
            channel.shutdown();
        }
        logger.debug("result "+(T)result);

    }

/*
    public <T>void write(Message message,  CallFuture<T> callback) throws Exception {
        try {
            if(connectivityState.equals(ConnectivityState.READY) || connectivityState.equals(ConnectivityState.IDLE)) {
                StreamObserver<Object> observerAdpater = new CallbackToResponseStreamObserverAdpater<>(callback, channel);
                Object result = client.execute(message);


                MethodDescriptor<Object[], Object> m = ServiceDescriptor.create(SerializerService.class).getMethod("execute", MethodDescriptor.MethodType.UNARY);
                StreamObserver<Object> observerAdpater = new CallbackToResponseStreamObserverAdpater<>(callback, channel);
                ClientCalls.asyncUnaryCall(channel.newCall(m, CallOptions.DEFAULT), new Object[]{message}, observerAdpater);
                logger.debug("successfully execute message " + message.toJson());
                logger.debug("callback value "+callback.getResult()+" get error "+callback.getError());
            }
        }catch (Exception e){
            logger.error("fail execute async  message "+message.toJson(),e);
        }
    }


 */



}
