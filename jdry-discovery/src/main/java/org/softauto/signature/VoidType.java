package org.softauto.signature;

public class VoidType implements TypeInterface{

    @Override
    public Object apply(Object o) {
        return o;
    }
}
