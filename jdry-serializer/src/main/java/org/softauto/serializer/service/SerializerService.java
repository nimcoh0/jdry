package org.softauto.serializer.service;

import org.apache.avro.Protocol;
import org.apache.avro.ipc.CallFuture;

import java.nio.ByteBuffer;


public interface SerializerService {
  public static final Protocol PROTOCOL = Protocol.parse("{\"protocol\":\"SerializerService\",\"namespace\":\"tests.infrastructure\",\"version\":\"1.0.0\",\"types\":[],\"messages\":{\"execute\":{\"request\":[{\"name\":\"message\",\"type\":\"bytes\"}],\"response\":{\"type\":\"bytes\"}}}}");


  Object execute(ByteBuffer message) throws Exception;


  public interface Callback extends SerializerService {
    public static final Protocol PROTOCOL = SerializerService.PROTOCOL;
    void execute(ByteBuffer message, CallFuture<Object> callback) throws Exception;
  }
}