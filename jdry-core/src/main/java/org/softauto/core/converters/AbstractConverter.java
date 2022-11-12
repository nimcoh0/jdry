package org.softauto.core.converters;

public abstract class AbstractConverter {

    Object value;

    Class type;


    public Object getValue() {
        return value;
    }

    public AbstractConverter setValue(Object value) {
        this.value = value;
        return this;
    }

    public Class getType() {
        return type;
    }

    public AbstractConverter setType(Class type) {
        this.type = type;
        return this;
    }

    public abstract Object build();
}
