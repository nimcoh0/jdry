package org.softauto.injector;


public interface CallerHandler {
        Object[] startCall(String fullClassName,Object[] args,Class[] types);

}
