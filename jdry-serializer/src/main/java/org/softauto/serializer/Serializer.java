package org.softauto.serializer;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.*;
import org.apache.avro.ipc.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
//import org.softauto.core.Callback;
import org.softauto.serializer.grpc.AvroGrpcClient;
import org.softauto.serializer.service.Message;
import org.softauto.serializer.service.SerializerService;
import java.nio.ByteBuffer;

public class Serializer {

    private static final Logger logger = LogManager.getLogger(Serializer.class);
    private static final Marker JDRY = MarkerManager.getMarker("JDRY");
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
        this.clientOneWay = (SerializerService) AvroGrpcClient.create(this.channel, SerializerService.class);
        return this;
    }

    public Serializer build(){
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        connectivityState = channel.getState(true);
        clientOneWay = AvroGrpcClient.create(channel, SerializerService.class);
        clientTwoWay = AvroGrpcClient.create(channel, SerializerService.Callback.class);
        logger.debug(JDRY,"channel build using host "+ host + " and port "+ port);
        return this;
    }

    public <T> Object write(Message message) throws Exception {
        Object result = null;
        try {
            if(connectivityState.equals(ConnectivityState.READY) || connectivityState.equals(ConnectivityState.IDLE)) {
                byte[] m = new ObjectMapper().writeValueAsBytes(message);
                ByteBuffer byteBuffer = ByteBuffer.wrap(m);
                result =  clientOneWay.execute(byteBuffer);
                logger.debug(JDRY,"successfully execute message " + message.toJson());
            }
        }catch (Exception e){
            logger.error(JDRY,"fail execute sync message "+ message.toJson(),e);
        }finally {
           channel.shutdown();
        }
        logger.debug(JDRY,"result "+(T)result);
        return (T)result;
    }




    public <RespT> void write(Message message, Callback<RespT> callback) throws Exception {
        try {
            if(connectivityState.equals(ConnectivityState.READY) || connectivityState.equals(ConnectivityState.IDLE)) {
                byte[] m = new ObjectMapper().writeValueAsBytes(message);
                ByteBuffer byteBuffer = ByteBuffer.wrap(m);
                clientTwoWay.execute(byteBuffer,callback);

                logger.debug(JDRY,"successfully execute message " + message.toJson());
            }
        }catch (Exception e){
            logger.error(JDRY,"fail execute sync message "+ message.toJson(),e);
        }
    }





}
