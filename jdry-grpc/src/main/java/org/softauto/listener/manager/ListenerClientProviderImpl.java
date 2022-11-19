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
        try {
            ListenerServiceImpl listenerServiceImpl = new ListenerServiceImpl();
            Listener.init(listenerServiceImpl);
            logger.info("successfully load listener manager");
        }catch (Exception e){
            logger.error("fail to load listener manager",e);
        }
        return this;
    }



    public void register() {
        ServiceLocator.getInstance().register("LISTENER-MANAGER",this);
    }


}
