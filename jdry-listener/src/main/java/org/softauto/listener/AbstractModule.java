package org.softauto.listener;

public abstract class AbstractModule {

    public abstract void configuration();

    public void listen(String fqmn,Object[] types){
        Listeners.addListener(fqmn,types);
    }


}
