package org.softauto.signature;

public class Parameterized implements ResultInterface{

    String parameterized ;

    @Override
    public ResultInterface apply(Object o) {
        this.parameterized = o.toString();
        return this;
    }

    @Override
    public String getResult() {
        return parameterized;
    }
}
