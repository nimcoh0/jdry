package org.softauto.jaxrs.auth;

import org.softauto.jaxrs.AbstractStepDescriptorImpl;
import org.softauto.jaxrs.annotations.AuthenticationType;
import org.softauto.jaxrs.auth.jakarta.basic.BasicStepDescriptorImpl;
import org.softauto.jaxrs.auth.jakarta.jwt.JwtStepDescriptorImpl;
import org.softauto.jaxrs.auth.jakarta.none.NoneStepDescriptorImpl;
import org.softauto.jaxrs.service.RestService;

public class AuthFactory {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(AuthFactory.class);

    public static AbstractStepDescriptorImpl getStepDescriptor(String authenticationType){
        if(authenticationType.equals(AuthenticationType.JWT.getValue()) ){
            logger.debug("authentication Type set to JWT" );
            return new JwtStepDescriptorImpl();
        }else if(authenticationType.equals(AuthenticationType.BASIC.getValue()) ){
            logger.debug("authentication Type set to BASIC" );
            return new BasicStepDescriptorImpl();
        }else if(authenticationType.equals(AuthenticationType.NONE.getValue()) ){
            logger.debug("authentication Type set to NONE" );
            return new NoneStepDescriptorImpl();
        }else {
            logger.error("authentication Type not supported " + authenticationType);
        }

        return null;
    }


}
