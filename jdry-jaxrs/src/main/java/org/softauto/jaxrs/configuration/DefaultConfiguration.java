package org.softauto.jaxrs.configuration;

import org.softauto.jaxrs.annotations.AuthenticationType;
import org.softauto.jaxrs.security.auth.basic.BasicStepDescriptorImpl;


import java.util.HashMap;

public class DefaultConfiguration {

    static HashMap<String,Object> configuration = new HashMap<>();

    static{
         configuration.put(Context.STEP_DESCRIPTOR_IMPL_CLASS, new BasicStepDescriptorImpl());
         configuration.put(Context.SESSION, false);
         configuration.put(Context.AUTH, AuthenticationType.BASIC);

    }

    public static HashMap<String,Object> getConfiguration() {
        return configuration;
    }
}
