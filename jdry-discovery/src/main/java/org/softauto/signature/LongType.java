package org.softauto.signature;

public class LongType implements TypeInterface{
    @Override
    public Object apply(Object o) {
        return "Ljava/lang/Long;";
    }
}
