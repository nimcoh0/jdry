package org.softauto.injector;

public class ServiceCaller {

    public static CallerHandler call(UnaryClass clazz) {
        return new Call(clazz);
    }

    public static VarHandler InitVar(requestVar clazz) {
        return new InitVar(clazz);
    }

    public interface requestClass {
        Object[] invoke(String fullClassName,Object[] args,Class[] types);
    }

    public interface requestVar {
        Object invoke(String fullClassName,String var,Object value);
    }

    public interface UnaryClass extends requestClass {
    }

    public static  class Call implements CallerHandler {
        private  requestClass clazz;


        Call(requestClass clazz) {
            this.clazz = clazz;
        }

        @Override
        public Object[] startCall(String fullClassName, Object[] args,Class[] types) {
            return  this.clazz.invoke(fullClassName,args,types);
        }
    }

    public static  class InitVar implements VarHandler {
        private  requestVar clazz;

        InitVar(requestVar clazz) {
            this.clazz = clazz;
        }

        @Override
        public Object startCall(String fullClassName, String var, Object value) {
            return  this.clazz.invoke(fullClassName,var,value);
        }
    }



}
