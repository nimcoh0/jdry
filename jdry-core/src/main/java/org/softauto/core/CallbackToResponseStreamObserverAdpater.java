package org.softauto.core;

import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;


import java.io.Serializable;

public class CallbackToResponseStreamObserverAdpater<T> implements StreamObserver<Object>, Serializable {
    private org.softauto.core.Callback<Object> callback;
    private ManagedChannel channel;
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(CallbackToResponseStreamObserverAdpater.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");


    public CallbackToResponseStreamObserverAdpater(org.softauto.core.Callback<Object> callback, ManagedChannel channel) {
        this.callback = callback;
        this.channel = channel;
    }



    @Override
    public void onNext(Object value) {
        if(value instanceof Byte[] && ((Byte[]) value).length == 1){
            value = null;
        }
        if (value instanceof Throwable) {
            callback.handleError((Throwable) value);
        } else {
            callback.handleResult((T) value);
        }
    }

    @Override
    public void onError(Throwable t) {
        callback.handleError(new Exception(t));
        if(channel != null)
            channel.shutdown();
    }

    @Override
    public void onCompleted() {
        if(channel != null)
         channel.shutdown();
        this.callback.handleResult((T)new Object());

    }

    public void onCompleted(T res) {
        if(channel != null)
            channel.shutdown();
        this.callback.handleResult((T)res);

    }
}
