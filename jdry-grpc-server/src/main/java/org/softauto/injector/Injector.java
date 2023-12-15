package org.softauto.injector;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.core.ClassType;
import org.softauto.core.Utils;

/**
 * Injector  new class instance
 */
public class Injector  {

    private static final Logger logger = LogManager.getLogger(Injector.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");


    public  Object[] inject(String fullClassName, Object[] args, Class[] types, ClassType classType){
        try {
            Object[] objs = ServiceCaller.call(InitializerFactory.getInitializer(classType)).startCall(fullClassName,args,types);
            logger.debug(JDRY,"successfully inject "+ fullClassName + " with args "+ Utils.result2String(args));
            return objs;
        }catch (Exception e){
            logger.error(JDRY,"fail inject "+ fullClassName+ " with args "+ Utils.result2String(args),e);
        }
        return null;
    }


}
