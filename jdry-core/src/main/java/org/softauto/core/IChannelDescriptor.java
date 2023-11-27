package org.softauto.core;

import java.util.HashMap;

public interface IChannelDescriptor {

    void setHost(String host) ;

    String getHost();

    void setPort(int port) ;

    int getPort();

    void setProtocol(String protocol) ;

    String getProtocol();

    HashMap<String,Object> getProperties();

    void addProperty(String key,Object value);

    void setProperties(HashMap<String,Object> properties);

    }
