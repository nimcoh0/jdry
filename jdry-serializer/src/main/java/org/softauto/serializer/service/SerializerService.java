package org.softauto.serializer.service;

import org.apache.avro.Protocol;
import org.apache.avro.ipc.CallFuture;

import java.nio.ByteBuffer;


public interface SerializerService {
  public static final Protocol PROTOCOL = Protocol.parse("{\"protocol\":\"SerializerService\",\"namespace\":\"tests.infrastructure\",\"version\":\"1.0.0\",\"types\":[],\"messages\":{\"execute\":{\"request\":[{\"name\":\"message\",\"type\":\"bytes\"}],\"response\":{\"type\":\"bytes\"}}}}");


  Object execute(ByteBuffer message) throws Exception;

  @SuppressWarnings("all")
  public interface Callback extends SerializerService {
    public static final Protocol PROTOCOL = SerializerService.PROTOCOL;
    <RespT> void execute(ByteBuffer message, org.apache.avro.ipc.Callback<RespT> callback) throws Exception;
  }
}