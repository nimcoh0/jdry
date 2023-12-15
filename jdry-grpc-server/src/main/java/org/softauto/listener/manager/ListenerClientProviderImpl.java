package org.softauto.listener.manager;


import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.core.ServiceLocator;
import java.io.IOException;


public class ListenerClientProviderImpl  {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ListenerClientProviderImpl.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    private static ListenerClientProviderImpl listenerClientProviderImpl = null;


    String type = "LISTENER-MANAGER";

    public static ListenerClientProviderImpl getInstance(){
        if(listenerClientProviderImpl == null){
            listenerClientProviderImpl =  new ListenerClientProviderImpl();
        }
        return listenerClientProviderImpl;
    }


    public ListenerClientProviderImpl initialize() throws IOException {
        return this;
    }



    public void register() {
        ServiceLocator.getInstance().register("LISTENER-MANAGER",this);
    }


}
