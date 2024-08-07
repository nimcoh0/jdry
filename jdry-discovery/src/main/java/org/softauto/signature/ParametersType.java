package org.softauto.signature;

import java.util.HashMap;
import java.util.LinkedList;

public class ParametersType implements ResultInterface{

    LinkedList<HashMap<String,Boolean>> result = new LinkedList<>();

    @Override
    public ResultInterface apply(Object o) {
        HashMap<String,Boolean> m = new HashMap() ;

        if(o.toString().startsWith("T")){
            m.put(o.toString(),true);
        }else {
            m.put(o.toString(),false);
        }
        this.result.add(m);
        return this;
    }



    @Override
    public LinkedList<HashMap<String,Boolean>> getResult() {
        return result;
    }
}
