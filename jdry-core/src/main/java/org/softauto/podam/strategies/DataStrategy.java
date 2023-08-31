package org.softauto.podam.strategies;


import org.softauto.espl.Espl;
import uk.co.jemos.podam.common.AttributeStrategy;

import java.util.HashMap;


public class DataStrategy implements AttributeStrategy<Object> {

    String expression;

    String attribute;

    public DataStrategy(String expression,String attribute){
        this.expression = expression;
        this.attribute = attribute;
    }

    @Override
    public HashMap<String, Object> getValue() {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put(attribute,expression);
        return hm;
    }

}
