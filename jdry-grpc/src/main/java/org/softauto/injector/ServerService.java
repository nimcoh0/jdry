package org.softauto.injector;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.core.Utils;
import org.softauto.injector.jvm.HeapHelper;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;


/**
 * create the injector service
 * currently only Singleton,class with arg in the constructor & class with no arg in the constructor are supported
 */
public class ServerService {

    private static final Logger logger = LogManager.getLogger(ServerService.class);


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
                logger.debug("Class Initialize  " + fullClassName);
            }catch (Exception e){
                logger.warn("fail Initialize Class  "+fullClassName,e.getMessage());
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

                    Object[] objects = HeapHelper.getInstances(c);
                    if(objects != null && objects.length > 0){
                        logger.debug("found "+objects.length+" instances in jvm. for class "+ fullClassName);
                        return objects;
                    }

                Method singleton =  c.getDeclaredMethod("getInstance");
                obj  = singleton.invoke(c);
                logger.debug("invoke Singleton class  " + fullClassName);
             }catch (Exception e){
                logger.warn("fail get Instance Class for  "+fullClassName);

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
                Object[] objects = HeapHelper.getInstances(c);
                    if(objects != null && objects.length > 0){
                        logger.debug("found "+objects.length+" instances in jvm. for class "+ fullClassName);
                        return objects;
                    }
                if(types.length > 0) {
                    Constructor constructor = c.getDeclaredConstructor(types);
                    obj = constructor.newInstance(args);
                }else {

                    obj =  c.getConstructors()[0].newInstance();
                }
                logger.debug("invoke Initialize class "+fullClassName + "with ares "+Utils.result2String(args) + "and types "+Utils.result2String(types));
            }catch (Exception e){
                logger.warn("fail get Instance Class for  "+fullClassName,e.getMessage());
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
                logger.debug("invoke Initialize class "+fullClassName + "with ares "+Utils.result2String(args) + "and types "+Utils.result2String(types));
            }catch (Exception e){
                logger.warn("fail get Instance Class for  "+fullClassName,e.getMessage());
            }
            return new Object[]{obj};
        }


    }

    /**
     * Handler for class with no arg in the constructor
     */
    /*
    public static class InitializeNoParamClassHandler implements ServiceCaller.UnaryClass  {

        @Override
        public Object[] invoke(String fullClassName, Object[] args,Class[] types) {
            Object obj = null;
            try {
                Class c = Utils.findClass(fullClassName) ;
                if (Boolean.valueOf(Configuration.get(Context.USE_ADVANCED_INJECTOR)) ){
                    Object[] objects = HeapHelper.getInstances(c);
                    if(objects != null && objects.length > 0){
                        logger.debug("found "+objects.length+" instances in jvm. for class "+ fullClassName);
                        return objects;
                    }
                }
                obj  = c.newInstance();
                logger.debug("invoke Initialize No Param Class "+ fullClassName+ " using classLoader "+ c.getClassLoader());
            }catch (Exception e){
                logger.warn("fail get Instance Class for  "+fullClassName,e.getMessage());
            }
            return new Object[]{obj};
        }
    }


     */

     /*
    public static class VariableHandler implements ServiceCaller.requestVar  {

        @Override
        public Object invoke(String fullClassName, String var, Object value) {
            Object[] objects = null;
            try {
               Class c = Utils.findClass(fullClassName) ;
               if (Boolean.valueOf(Configuration.get(Context.USE_ADVANCED_INJECTOR)) ){
                   objects = HeapHelper.getInstances(c);
                   if(objects != null && objects.length > 0){
                       logger.debug("found "+objects.length+" instances in jvm. for class "+ fullClassName);
                       for(Field field : objects[0].getClass().getDeclaredFields()){
                          if(field.getName().equals(var)){
                              field.setAccessible(true);
                              field.set(objects[0], value);
                              break;
                          }
                      }
                       return objects;
                   }
               }
           }catch (Exception e){
               logger.warn("fail set Variable "+fullClassName +"."+var,e.getMessage());
           }
            return objects[0];
        }
    }


      */
}
