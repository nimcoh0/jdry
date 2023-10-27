package org.softauto.analyzer.directivs.callback;

public class CallBack {

    OnSuccess onSuccess;

    OnFailure onFailure;

    String name;

    String type;

    boolean enabledAssert = false;

    public String getType() {
        return type;
    }

    public boolean isEnabledAssert() {
        return enabledAssert;
    }

    public void setEnabledAssert(boolean enabledAssert) {
        this.enabledAssert = enabledAssert;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public OnSuccess getOnSuccess() {
        return onSuccess;
    }

    public void setOnSuccess(OnSuccess onSuccess) {
        this.onSuccess = onSuccess;
    }

    public OnFailure getOnFailure() {
        return onFailure;
    }

    public void setOnFailure(OnFailure onFailure) {
        this.onFailure = onFailure;
    }
}
