package org.softauto.signature;

public class ByteType implements TypeInterface{
    @Override
    public Object apply(Object o) {
        return "Ljava/lang/Byte;";
    }
}
