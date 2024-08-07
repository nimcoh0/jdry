package org.softauto.signature;

public enum Types {

    CLASS(ClassType.class),
    BOOLEAN(BooleanType.class),
    SHORT(ShortType.class),
    BYTE(ByteType.class),
    DOUBLE(DoubleType.class),
    FLOAT(FloatType.class),
    LONG(LongType.class),
    CHAR(CharType.class),
    NONE(NoneType.class),
    ARRAY(ArrayType.class),
    GENERIC(GenericType.class),
    VOID(VoidType.class),
    PARAMETERIZED(Parameterized.class),
    INT(IntType.class);


    private Types(Class  value) {
        this.value = value;
    }



    private final Class value;


    public static Types fromString(String text) {
        for (Types b : Types.values()) {
            if (b.name().equals(text)) {
                return b;
            }
        }
        return null;
    }

    public Class getValue() {
        return value;
    }

    public String toString() {
        return this.value.toString();
    }

}
