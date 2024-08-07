package org.softauto.signature;

public class None implements ResultInterface{

    Object result;

    @Override
    public ResultInterface apply(Object o) {
        this.result = o;
        return this;
    }

    @Override
    public Object getResult() {
        return result;
    }
}
