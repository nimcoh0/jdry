package org.softauto.listener.impl;


import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class Utils {

    static org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Utils.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    public static String buildMethodFQMN(String methodName, String clazz){
        return clazz.replace(".","_")+"_"+methodName;
    }

    /**
     * get the arguments types
     **/
    public static Class[] getTypes(Object obj){
        Class[] types;
        if(obj instanceof Class<?>){
            types = new Class[1];
            types[0] = (Class)obj;
        }else {
            return (Class[])obj;
        }

        return types;
    }

    /**
     * get the arguments values
     **/
    public static Object[] getArgs(Object obj){
        Object[] args;

        if(!(obj instanceof Object[])){
            args = new Object[1];
            args[0] = obj;
        }else {
            return (Object[])obj;
        }

        return args;
    }


}
