package org.softauto.espl;

public class StepBuilder {

    public static Builder newBuilder() { return new Builder();}

    String expression;

    public StepBuilder(String step){
        this.expression = step;
    }

    public String getExpression(){
        return expression;
    }

    public static class Builder<T> {

        String response;

        String method;

        String args;

        String returnType;

        String fqmn;

        String value;

        String name;

        String valueType;

        String callOption;

        String protocol = "\"RPC\"";


        public Builder setCallOption(String callOption) {
            this.callOption = callOption;
            return this;
        }

        public Builder setProtocol(String protocol) {
            this.protocol = "\""+protocol+"\"";
            return this;
        }

        public Builder setReturnType(String returnType) {
            this.returnType = returnType;
            return this;
        }

        public Builder setFqmn(String fqmn) {
            this.fqmn = fqmn;
            return this;
        }

        public Builder setValue(String value) {
            this.value = value;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setValueType(String valueType) {
            this.valueType = valueType;
            return this;
        }

        public StepBuilder build(){
            if(value == null || value.isEmpty()){
                value = "{}";
            }else {
                value = "{"+ value +"}";
            }
            if(valueType == null){
                valueType = "{}";
            }else {
                valueType = "{"+(valueType)+".class}";
            }
            return new StepBuilder((returnType != null ? " ("+returnType+")" :"")
                    + "new Step(\""+fqmn.replace(".","_")+"\",new Object[]"+value+
                    ",new Class[]"+ valueType+"," +
                    (protocol != null ? protocol : "\"RPC\"") +
                    (callOption != null ? callOption : "")+
                    ").get_Result()");
        }
    }
}
