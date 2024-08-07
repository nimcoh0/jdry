package org.softauto.signature;

public class DoubleType implements TypeInterface{
    @Override
    public Object apply(Object o) {
        return "Ljava/lang/Double;";
    }
}
