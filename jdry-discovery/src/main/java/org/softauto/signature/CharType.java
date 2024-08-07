package org.softauto.signature;

public class CharType implements TypeInterface{
    @Override
    public Object apply(Object o) {
        return "Ljava/lang/Character;";
    }
}
