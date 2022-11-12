package org.softauto.serializer;


import io.grpc.*;
import org.apache.avro.grpc.AvroGrpcClient;
import org.apache.avro.ipc.CallFuture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.serializer.service.Message;
import org.softauto.serializer.service.SerializerService;


public class Serializer {

    private static final Logger logger = LogManager.getLogger(Serializer.class);
    String host;
    int port;
    ManagedChannel channel = null;
    SerializerService client = null;
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
        this.client = (SerializerService)AvroGrpcClient.create(this.channel, SerializerService.class);
        return this;
    }

    public Serializer build(){
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        connectivityState = channel.getState(true);
        client = AvroGrpcClient.create(channel, SerializerService.class);
        logger.debug("channel build using host "+ host + " and port "+ port);
        return this;
    }

    public <T> T write(Message message) throws Exception {
        Object result = null;
        try {
            if(connectivityState.equals(ConnectivityState.READY) || connectivityState.equals(ConnectivityState.IDLE)) {
                //byte[] m = new ObjectMapper().writeValueAsBytes(message);
                result = client.execute(message);
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


    public <T>void write(Message message,  CallFuture<T> callback) throws Exception {
        try {
            if(connectivityState.equals(ConnectivityState.READY) || connectivityState.equals(ConnectivityState.IDLE)) {

            }
        }catch (Exception e){
            logger.error("fail execute async  message "+message.toJson(),e);
        }
    }

    /*

    public <T>void write(Message message,  CallFuture<T> callback) throws Exception {
        try {
            if(connectivityState.equals(ConnectivityState.READY) || connectivityState.equals(ConnectivityState.IDLE)) {
                //MethodDescriptor<Object[], Object> m =  new ServiceDescriptor().getMethods(("execute", MethodDescriptor.MethodType.UNARY)
                //MethodDescriptor<Object[], Object> m = ServiceDescriptor.create(SerializerService.class).getMethod("execute", MethodDescriptor.MethodType.UNARY);
                Collection<MethodDescriptor<?, ?>> m = ServiceDescriptor.newBuilder("").setSchemaDescriptor(SerializerService.class).build().getMethods();
                        //.addMethod(MethodDescriptor.MethodType.UNARY);
                StreamObserver<Object> observerAdpater = new CallbackToResponseStreamObserverAdpater(callback, channel);
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
