package org.softauto.signature;

public class NoneType implements TypeInterface{


    @Override
    public Object apply(Object o) {
        return o;
    }
}
