package org.softauto.signature;

public class ShortType implements TypeInterface{
    @Override
    public Object apply(Object o) {
        return "Ljava/lang/Short;";
    }
}
