package org.softauto.jaxrs.util;

import org.softauto.jaxrs.service.ChannelDescriptor;

public class ChannelBuilder {

    public static Builder newBuilder (){
        return new Builder();
    }

    ChannelDescriptor channelDescriptor;

    public ChannelBuilder(ChannelDescriptor channelDescriptor){
        this.channelDescriptor = channelDescriptor;
    }

    public ChannelDescriptor getChannelDescriptor() {
        return channelDescriptor;
    }

    public static class Builder{

        ChannelDescriptor channelDescriptor;

        String host;

        String post ;

        String port;

        String protocol;

        String baseUrl;

        String path;

        Object[] args;


        public Builder setPort(String port) {
            this.port = port;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Builder setArgs(Object[] args) {
            this.args = args;
            return this;
        }

        public Builder setHost(String host) {
            this.host = host;
            return this;
        }

        public Builder setPost(String post) {
            this.post = post;
            return this;
        }

        public Builder setProtocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public ChannelBuilder build(){
            channelDescriptor =  ChannelDescriptor.newBuilder()
                    .setHost(host)
                    .setPort(Integer.valueOf(port))
                    .setProtocol(protocol)
                    .setBaseUrl(baseUrl)
                    .setPath(path)
                    .build((Object[]) args);
            return new ChannelBuilder(channelDescriptor);
        }


    }
}
