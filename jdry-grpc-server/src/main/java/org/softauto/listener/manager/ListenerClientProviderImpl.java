package org.softauto.listener.manager;


import org.softauto.core.ServiceLocator;
import org.softauto.listener.impl.*;
import java.io.IOException;


public class ListenerClientProviderImpl  {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ListenerClientProviderImpl.class);
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
