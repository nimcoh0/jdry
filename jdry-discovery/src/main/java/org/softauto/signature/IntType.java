package org.softauto.signature;

public class IntType implements TypeInterface{
    @Override
    public Object apply(Object o) {
        return "Ljava/lang/Integer;";
    }
}
