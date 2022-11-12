package org.softauto.core;

import java.lang.reflect.Method;
import java.util.Map;

public interface InvocationHandler {

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable;

    public Map<String, Object> getParams();
}
