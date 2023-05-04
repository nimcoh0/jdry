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

    public Object consume(String key){
        return suite.findKey(key);
    }

    public Object consume(String id,String expression,String type){
        String str = suite.getPublish(id , expression ).toString();
        return Utils.toObject(type,str);
    }
}
