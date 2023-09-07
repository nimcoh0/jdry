package org.softauto.core;


public class Suite extends AbstractSuite{


    @Override
    public <T> T getPublishParameter(String param) {
        return super.getPublish(param);
    }

    @Override
    public <T> T getPublishParameter(String param, String type) {
        return super.getPublish(param,type);
    }
}
