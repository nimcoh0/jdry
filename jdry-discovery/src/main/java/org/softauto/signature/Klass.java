package org.softauto.signature;

public class Klass implements ResultInterface{

    String result;

    public String getResult() {
        return result;
    }

    @Override
    public ResultInterface apply(Object o) {
        this.result = o.toString();
        return this;
    }
}
