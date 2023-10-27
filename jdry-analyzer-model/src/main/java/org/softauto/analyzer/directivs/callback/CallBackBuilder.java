package org.softauto.analyzer.directivs.callback;



public class CallBackBuilder {

    public static Builder newBuilder() { return new Builder();}

    CallBack callBack;

    public CallBackBuilder(CallBack callBack){
        this.callBack = callBack;
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public static class Builder  {

        String name;

        String type;

        boolean enabledAssert = false;

        String onFailureParameters;

        String onFailureExpression;

        String onFailureReturnType;

        String onSuccessParameters;

        String onSuccessExpression;

        String onSuccessReturnType;

        public Builder setEnabledAssert(boolean enabledAssert) {
            this.enabledAssert = enabledAssert;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setOnFailureParameters(String onFailureParameters) {
            this.onFailureParameters = onFailureParameters;
            return this;
        }

        public Builder setOnFailureExpression(String onFailureExpression) {
            this.onFailureExpression = onFailureExpression;
            return this;
        }

        public Builder setOnFailureReturnType(String onFailureReturnType) {
            this.onFailureReturnType = onFailureReturnType;
            return this;
        }

        public Builder setOnSuccessParameters(String onSuccessParameters) {
            this.onSuccessParameters = onSuccessParameters;
            return this;
        }

        public Builder setOnSuccessExpression(String onSuccessExpression) {
            this.onSuccessExpression = onSuccessExpression;
            return this;
        }

        public Builder setOnSuccessReturnType(String onSuccessReturnType) {
            this.onSuccessReturnType = onSuccessReturnType;
            return this;
        }

        public CallBackBuilder build(){
            OnSuccess onSuccess = new OnSuccess();
            OnFailure onFailure = new OnFailure();
            onSuccess.setExpression(onSuccessExpression);
            onSuccess.setParameters(onSuccessParameters);
            onSuccess.setReturnType(onSuccessReturnType);
            onFailure.setExpression(onFailureExpression);
            onFailure.setParameters(onFailureParameters);
            onFailure.setReturnType(onFailureReturnType);
            CallBack callBack = new CallBack();
            callBack.setOnFailure(onFailure);
            callBack.setOnSuccess(onSuccess);
            callBack.setName(name);
            callBack.setType(type);
            callBack.setEnabledAssert(enabledAssert);
            return new CallBackBuilder(callBack);
        }
    }
}
