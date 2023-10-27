package org.softauto.analyzer.core.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.base.Defaults;
import org.apache.commons.lang3.ClassUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
//import org.softauto.core.Handler;


public class CheckNotNullOrEmpty<T,R> {

    private static Logger logger = LogManager.getLogger(CheckNotNullOrEmpty.class);

    private T o ;

    private boolean isNull = false ;

    private boolean isEmpty  = false;

    private boolean isTrue = true;

    private String type;

    public CheckNotNullOrEmpty(T o){
        this.o = o;
    }

    public CheckNotNullOrEmpty setType(String type) {
        this.type = type;
        return this;
    }

    public static <T> CheckNotNullOrEmpty set(T o){
        return new CheckNotNullOrEmpty(o);
    }

    public CheckNotNullOrEmpty isNotNull(){
        try {
            if(o != null){
                isNull = false;
            }else {
                isNull = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public CheckNotNullOrEmpty isTrue(boolean statement){
        try {
           this.isTrue = statement;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return this;
    }

    public CheckNotNullOrEmpty isNotEmpty(){
        try {
            if(o != null) {
                JsonNode node = new ObjectMapper().readTree(o.toString());
                if (node.isArray()) {
                    if (((ArrayNode) node).size() > 0) {
                        isEmpty = false;
                        return this;
                    }
                } else {
                    if (node.isValueNode()) {
                        isEmpty = false;
                        return this;
                    }
                }
            }
            isEmpty = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public T get()throws Exception{
       if(isTrue && !isNull && !isEmpty){
         return o;
       }
       if(Utils.isPrimitive(type)) {
           Class c = ClassUtils.getClass(type);
           return (T) Defaults.defaultValue(c);
       }else {
           if(o == null){
               return o;
           }
           if(isEmpty){
               Class c = ClassUtils.getClass(type);
               Object obj =  new ObjectMapper().readValue(o.toString(),c);
               if(obj instanceof AbstractCollection){
                   return (T)("new "+c.getTypeName()+"();");
               }
           }
           return o;
       }
    }





    public <T> CheckNotNullOrEmpty onSuccess(Handler<T,R> resultHandler){
        if(isTrue && !isNull && !isEmpty){
            try {
                resultHandler.handle((T) o);
            } catch (Exception e) {
                logger.error("fail handel onSuccess ",e);
            }
        }
        return this;
    }

    public CheckNotNullOrEmpty onFail(Handler<Exception,String> resultHandler){
        if(!isTrue || isNull || isEmpty) {
            try {
                resultHandler.handle( new Exception(""));
            } catch (Exception e) {
                logger.error("fail handel onFail ",e);
            }
        }
        return this;
    }

}
