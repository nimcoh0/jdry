package org.softauto.plugin.api;

import com.fasterxml.jackson.databind.JsonNode;
import io.grpc.ManagedChannel;
//import org.softauto.core.CallFuture;
import org.apache.avro.ipc.CallFuture;


import javax.lang.model.element.Element;
import java.io.IOException;
import java.util.HashMap;

public interface Provider {

     /**
      * initilize plugin
      * @return
      * @throws IOException
      */
     Provider initialize()throws IOException;

     /**
      * register plugin in the ServiceLocator
      */
     void register();

     /**
      * shutdown plugin
      */
     void shutdown();


     /**
      * get this plugin type
      * @return
      */
     String getType();

     /**
      * parse new element of this provider type
      * @param element
      * @return
      */
     JsonNode parser(Element element);

     /**
      * set schema interface class
      * @param iface
      * @return
      */
     Provider iface(Class iface);

     /**
      * execute this plugin protocol request
      * @param name
      * @param args
      * @param callback
      * @param <RespT>
      */
     //<RespT> void exec(String name, org.softauto.serializer.CallFuture<RespT> callback, ManagedChannel channel,Object...args);
     <RespT> void exec(String name, CallFuture<RespT> callback, ManagedChannel channel, Object[] args, Class[] types, HashMap<String,Object> callOptions);
}
