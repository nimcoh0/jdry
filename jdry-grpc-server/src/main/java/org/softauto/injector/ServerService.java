package org.softauto.injector;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.core.Utils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;


/**
 * create the injector service
 * currently only Singleton,class with arg in the constructor & class with no arg in the constructor are supported
 */
public class ServerService {

    private static final Logger logger = LogManager.getLogger(ServerService.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");


    /**
     * Handler for class that we don't know how to load it
     */
    public static class NoneHandler implements ServiceCaller.UnaryClass  {
        @Override
        public Object[] invoke(String fullClassName, Object[] args,Class[] types) {
            Object obj = null;
            try {
                Class c = Class.forName(fullClassName);
                obj  = c.getConstructors()[0].newInstance();
                logger.debug(JDRY,"Class Initialize  " + fullClassName);
            }catch (Exception e){
                logger.warn(JDRY,"fail Initialize Class  "+fullClassName,e.getMessage());
            }
            return new Object[]{obj};
        }


    }


    /**
     * Handler for Singleton class
     */
    public static class SingletonClassHandler implements ServiceCaller.UnaryClass  {

        @Override
        public Object[] invoke(String fullClassName, Object[] args,Class[] types) {
            Object obj = null;
            try {
                Class c = Class.forName(fullClassName);
                Method singleton =  c.getDeclaredMethod("getInstance");
                obj  = singleton.invoke(c);
                logger.debug(JDRY,"invoke Singleton class  " + fullClassName);
             }catch (Exception e){
                logger.warn(JDRY,"fail get Instance Class for  "+fullClassName);

            }
            return new Object[]{obj};
        }


    }



    /**
     * Handler for class with arg in the constructor
     */
    public static class InitializeIfNotExistClassHandler implements ServiceCaller.UnaryClass  {

        @Override
        public Object[] invoke(String fullClassName, Object[] args,Class[] types) {
            Object obj = null;
            try {
                Class c = Class.forName(fullClassName);
                if(types.length > 0) {
                    Constructor constructor = c.getDeclaredConstructor(types);
                    obj = constructor.newInstance(args);
                }else {

                    obj =  c.getConstructors()[0].newInstance();
                }
                logger.debug(JDRY,"invoke Initialize class "+fullClassName + "with ares "+Utils.result2String(args) + "and types "+Utils.result2String(types));
            }catch (Exception e){
                logger.warn(JDRY,"fail get Instance Class for  "+fullClassName,e.getMessage());
            }
            return new Object[]{obj};
        }


    }

    public static class InitializeEveryTimeClassHandler implements ServiceCaller.UnaryClass  {

        @Override
        public Object[] invoke(String fullClassName, Object[] args,Class[] types) {
            Object obj = null;
            try {
                Class c = Class.forName(fullClassName);
                if(types.length > 0) {
                    Constructor constructor = c.getDeclaredConstructor(types);
                    obj = constructor.newInstance(args);
                }else {
                    obj = c.getConstructors()[0].newInstance();
                }
                logger.debug(JDRY,"invoke Initialize class "+fullClassName + "with ares "+Utils.result2String(args) + "and types "+Utils.result2String(types));
            }catch (Exception e){
                logger.warn(JDRY,"fail get Instance Class for  "+fullClassName,e.getMessage());
            }
            return new Object[]{obj};
        }


    }


}
