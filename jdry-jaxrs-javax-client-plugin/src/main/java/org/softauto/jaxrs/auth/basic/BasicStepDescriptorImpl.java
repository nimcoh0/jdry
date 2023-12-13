package org.softauto.jaxrs.auth.basic;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.softauto.core.TestContext;
import org.softauto.jaxrs.AbstractStepDescriptorImpl;
import java.util.Base64;

/**
 * impl Step Descriptor for basic auth
 */
public class BasicStepDescriptorImpl extends AbstractStepDescriptorImpl {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(BasicStepDescriptorImpl.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    @Override
    public Cookie getCookie(){
        Cookie cookie = null;
        try {
            if(TestContext.getScenario().getProperty("JSESSIONID") != null){
                cookie = (Cookie) TestContext.getScenario().getProperty("JSESSIONID");
            }
            logger.debug(JDRY,"successfully build cookie ");
        } catch (Exception e) {
            logger.error(JDRY,"fail build cookie",e);
        }
        return cookie;
    }

    @Override
    public Client getClient() {
        Client client = null;
        try {
            ClientConfig clientConfig = new ClientConfig();
            clientConfig.register(JacksonFeature.class);
            clientConfig.register(ResponseClientFilter.class);
            clientConfig.register(MultiPartFeature.class);
            client =  javax.ws.rs.client.ClientBuilder.newBuilder().withConfig((javax.ws.rs.core.Configuration) clientConfig).build();
            logger.debug(JDRY,"successfully build client ");
        }catch (Exception e){
            logger.error(JDRY,"fail build client ",e);
        }
        return client;
    }

    @Override
    public MultivaluedMap<String, Object> getHeaders() {
        MultivaluedMap<String, Object> mm = new MultivaluedHashMap<>();
        try {
            if(TestContext.getScenario() != null ) {
                mm.add("scenarioId", scenarioId);
                Object jsessioId = TestContext.getScenario().getProperty("JSESSIONID");
                if(jsessioId == null){
                    Object[] args =  (Object[])TestContext.getScenario().getProperty("args");
                    if(args != null && args.length ==2) {
                        String valueToEncode = args[0] + ":" + args[1];
                        String pass = "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
                        mm.add("Authorization", pass);
                    }
                }
            }
            if(callOptions != null && callOptions.containsKey("headers") && callOptions.get("headers") != null){
                mm.putAll((MultivaluedMap<String, Object>)callOptions.get("headers"));
            }
            logger.debug(JDRY,"successfully build headers ");
        } catch (Exception e) {
            logger.error(JDRY,"fail build headers ",e);
        }
        return mm;
    }

}
