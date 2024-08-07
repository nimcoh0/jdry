package org.softauto.signature;

public class FloatType implements TypeInterface{
    @Override
    public Object apply(Object o) {
        return "Ljava/lang/Float;";
    }
}
