package org.softauto.injector;


import org.softauto.core.ClassType;

public class InitializerFactory {



    public static ServiceCaller.UnaryClass getInitializer(ClassType classType){
        switch (classType){
           case SINGLETON : return new ServerService.SingletonClassHandler();
           //case INITIALIZE_IF_NOT_EXIST : return new ServerService.InitializeIfNotExistClassHandler();
           case INITIALIZE_EVERY_TIME : return new ServerService.InitializeEveryTimeClassHandler();
           default: return new ServerService.NoneHandler();
        }
     }

}
