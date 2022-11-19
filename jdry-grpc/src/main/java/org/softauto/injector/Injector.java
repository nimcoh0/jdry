package org.softauto.injector;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.core.ClassType;
import org.softauto.core.Utils;
import org.softauto.injector.jvm.HeapHelper;

/**
 * Injector  new class instance
 */
public class Injector  {

    private static final Logger logger = LogManager.getLogger(Injector.class);


    public  Object[] inject(String fullClassName, Object[] args, Class[] types, ClassType classType){
        try {
            Object[] objs = ServiceCaller.call(InitializerFactory.getInitializer(classType)).startCall(fullClassName,args,types);
            logger.debug("successfully inject "+ fullClassName + " with args "+ Utils.result2String(args));
            return objs;
        }catch (Exception e){
            logger.error("fail inject "+ fullClassName+ " with args "+ Utils.result2String(args),e);
        }
        return null;
    }

    /*
    public  Object inject(String fullClassName,String var,Object value){
        try {
            Object objs = ServiceCaller.InitVar(new ServerService.VariableHandler()).startCall(fullClassName,var,value);
            logger.debug("successfully inject "+ fullClassName + " with var "+ var + " value "+ value);
            return objs;
        }catch (Exception e){
            logger.error("fail inject "+ fullClassName+  " with var "+ var + " value "+ value,e);
        }
        return null;
    }


     */
    public Object[] getInstances(String fullClassName){
        try{
            Class c = Utils.findClass(fullClassName) ;
            return HeapHelper.getInstances(c);
        }catch (Exception e){
            logger.error("fail getting class in jvm " + fullClassName);
        }
        return null;
    }

    public void executeGC(){
        HeapHelper.clean();
    }
}
