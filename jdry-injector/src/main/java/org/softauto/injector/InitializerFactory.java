package org.softauto.injector;


import org.softauto.core.ClassType;

public class InitializerFactory {



    public static ServiceCaller.UnaryClass getInitializer(ClassType classType){
        switch (classType){
           case INITIALIZE_NO_PARAM : return new ServerService.InitializeNoParamClassHandler ();
           case SINGLETON : return new ServerService.SingletonClassHandler();
           case INITIALIZE : return new ServerService.InitializeClassHandler();
           default: return new ServerService.NoneHandler();
        }
     }

}
