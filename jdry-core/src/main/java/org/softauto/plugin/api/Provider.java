package org.softauto.plugin.api;

import com.fasterxml.jackson.databind.JsonNode;
import io.grpc.ManagedChannel;
//import org.softauto.core.CallFuture;
import org.apache.avro.ipc.CallFuture;
import org.apache.avro.ipc.Callback;
import org.softauto.core.IChannelDescriptor;
import org.softauto.core.IStepDescriptor;


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

     //IStepDescriptor buildStepDescriptor(String stepName, Object[] args, Class[] types, HashMap<String,Object> callOptions, String scenarioId, String auth, Callback<Object> callback);

     //IChannelDescriptor buildChannelDescriptor();

     //<RespT> void exec(String name, org.softauto.serializer.CallFuture<RespT> callback, ManagedChannel channel,Object...args);
     //<RespT> Object exec(IStepDescriptor testDescriptor) ;
     //<RespT> void exec(IStepDescriptor testDescriptor,org.apache.avro.ipc.Callback<Object> future);
     <RespT> Object exec(String name, ManagedChannel channel, Object[] args, Class[] types, HashMap<String,Object> callOptions,String scenarioId);
     <RespT> void exec(String name, org.apache.avro.ipc.Callback<RespT> callback, ManagedChannel channel, Object[] args, Class[] types, HashMap<String,Object> callOptions,String scenarioId);
}
