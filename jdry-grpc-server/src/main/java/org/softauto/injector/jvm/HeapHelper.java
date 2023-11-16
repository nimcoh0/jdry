package org.softauto.injector.jvm;

/**
 * this class provide native interface to JVM , for getting object handle that are already exist  . two methods are impl
 * findInstances : find object instances . will return list on instances
 * clean : invoke the GC
 */
public class HeapHelper {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(HeapHelper.class);



    private static native void cleanInstances();

    private static native <T> T[] findInstances(Class<T> klass);

    public static <T> T[]  getInstances(Class<T> klass){
        try {
            T[] t = (T[])findInstances(klass);
            logger.debug("found "+t.length+ " instances of "+ klass);
            return t;
        }catch (Throwable e){
            logger.warn("instances for "+ klass+ " not found");
        }
        return null;
    }


    public static void  clean(){
        try {
            cleanInstances();
        }catch (Throwable e){
            logger.warn("clean instances for fail",e.getMessage());
        }
    }
}
