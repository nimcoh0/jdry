package org.softauto.injector.core;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class Utils {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Utils.class);

    public static String result2String(Object result){
        try{

            if(result != null){
                if(result instanceof List){
                    return ToStringBuilder.reflectionToString(((List)result).toArray(), new org.softauto.injector.core.MultipleRecursiveToStringStyle());
                }else {
                    return ToStringBuilder.reflectionToString(result, new MultipleRecursiveToStringStyle());
                }
            }
        }catch(Exception e){
            logger.warn("result to String fail on  ",e.getMessage());
        }
        return "";
    }

    /**
     * get class by currentThread
     * @param fullClassName
     * @return
     */
    public static Class findClass(String fullClassName){
        try{
            Class c =   Thread.currentThread().getContextClassLoader().loadClass(fullClassName);
            return c;
        }catch (Exception e){
            logger.error("find Class fail " + fullClassName,e);
        }
        return null;
    }
}
