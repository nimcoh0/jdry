package org.softauto.signature;

public class BooleanType implements TypeInterface{
    @Override
    public Object apply(Object o) {
        return "Ljava/lang/Boolean;";
    }
}
