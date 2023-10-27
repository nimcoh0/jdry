package org.softauto.analyzer.model.expected;

public class ExpectedBuilder {

    public static Builder newBuilder() { return new Builder();}

    Expected expected;

    public ExpectedBuilder(Expected expected){
        this.expected = expected;
    }

    public Expected getExpected() {
        return expected;
    }

    public static class Builder  {

        String type;

        String name;

        String value;

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setValue(String value) {
            this.value = value;
            return this;
        }

        public ExpectedBuilder build(){
            Expected expected = new Expected();
            expected.setName(name);
            expected.setType(type);
            expected.setValue(value);
            return new ExpectedBuilder(expected);
        }
    }
}
