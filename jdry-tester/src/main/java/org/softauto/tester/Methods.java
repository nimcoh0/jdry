package org.softauto.tester;

import org.softauto.core.Utils;

import java.util.Date;

public class Methods {

    public Suite suite = new Suite();

    public boolean compare(Object expected,Object actual){
        return new Comparator().setExpected(expected).setActual(actual).compare();
    }

    public Object map(String reference){
        return suite.getPath(reference);
    }

    public long timestamp(){
        return new Date().getTime();
    }



    public Object consume(String expression){
        if(expression.contains("/")) {
            return suite.getPublish(expression);
        }else {
            return suite.findKey(expression);
        }
        //return Utils.toObject(type,str);
    }

    public Object consume(String id,String expression,String type){
        return suite.getPublish(id , expression ).toString();
        //return Utils.toObject(type,str);
    }

    public Object consume(String expression,String type){
        return suite.getPath(expression ).toString();
        //return Utils.toObject(type,str);
    }
}
