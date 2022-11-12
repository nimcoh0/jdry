package org.softauto.listener;

public interface ListenerService {

    Object[] executeBefore(String methodName, Object[] args, Class[] types) throws Exception ;
    Object executeAfter(String methodName, Object[] args, Class[] types) throws Exception ;

}
