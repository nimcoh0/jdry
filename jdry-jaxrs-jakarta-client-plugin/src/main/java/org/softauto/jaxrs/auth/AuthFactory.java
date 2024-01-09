package org.softauto.jaxrs.auth;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.jaxrs.AbstractStepDescriptorImpl;
//import org.softauto.jaxrs.annotations.AuthenticationType;
import org.softauto.jaxrs.annotations.AuthenticationType;
import org.softauto.jaxrs.auth.basic.BasicStepDescriptorImpl;
import org.softauto.jaxrs.auth.jwt.JwtStepDescriptorImpl;
import org.softauto.jaxrs.auth.none.NoneStepDescriptorImpl;

public class AuthFactory {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(AuthFactory.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    public static AbstractStepDescriptorImpl getStepDescriptor(String authenticationType){
        if(authenticationType.equals(AuthenticationType.JWT.getValue()) ){
            logger.debug(JDRY,"authentication Type set to JWT" );
            return new JwtStepDescriptorImpl();
        }else if(authenticationType.equals(AuthenticationType.BASIC.getValue()) ){
            logger.debug(JDRY,"authentication Type set to BASIC" );
            return new BasicStepDescriptorImpl();
        }else if(authenticationType.equals(AuthenticationType.NONE.getValue()) ){
            logger.debug(JDRY,"authentication Type set to NONE" );
            return new NoneStepDescriptorImpl();
        }else {
            logger.error(JDRY,"authentication Type not supported " + authenticationType);
        }

        return null;
    }


}
