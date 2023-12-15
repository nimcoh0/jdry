package org.softauto.jaxrs.service;


import jakarta.ws.rs.core.UriBuilder;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.net.URI;


public class ChannelDescriptor  {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ChannelDescriptor.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    public static Builder newBuilder() { return new Builder();}

    public ChannelDescriptor(URI uri){
        this.uri = uri;
    }

    private URI uri;

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public URI getUri() {
       return uri;
    }



    public static class Builder{

        private String host;

        private int port;

        private String protocol;

        private String baseUrl;

        private  String path;

        public Builder setHost(String host) {
            this.host = host;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
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

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public ChannelDescriptor build(Object[] args){
            URI uri = null;
            try {
                if(!path.startsWith("/")){
                    path = "/" + path;
                }
                if(baseUrl != null){
                    uri = UriBuilder.fromUri(protocol+"://"+host+":"+port+ baseUrl + path).build(args);
                }else {
                    uri = UriBuilder.fromUri(protocol + "://" + host + ":" + port + path).build(args);
                }
            }catch (Exception e){
                logger.error(JDRY ,"fail create uri");
            }
            return new ChannelDescriptor(uri);
        }

    }



}
