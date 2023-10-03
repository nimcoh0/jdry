package org.softauto;

import com.fasterxml.jackson.databind.JsonNode;
import org.softauto.auth.basic.BasicStepDescriptorImpl;
import org.softauto.auth.jwt.JwtStepDescriptorImpl;
import org.softauto.auth.none.NoneStepDescriptorImpl;
import org.softauto.core.Configuration;
import org.softauto.core.TestContext;
import org.softauto.jaxrs.AbstractStepDescriptorImpl;
import org.softauto.jaxrs.annotations.AuthenticationType;
import org.softauto.plugin.api.Provider;
import javax.lang.model.element.Element;
import java.io.IOException;
import java.util.HashMap;

public class RsPluginImpl implements Provider {

    public AbstractStepDescriptorImpl getStepDescriptor(String authenticationType){
        if(authenticationType.equals(AuthenticationType.BASIC.getValue())){
            return new BasicStepDescriptorImpl();
        }
        if(authenticationType.equals(AuthenticationType.JWT.getValue())){
            return new JwtStepDescriptorImpl();
        }
        if(authenticationType.equals(AuthenticationType.NONE.getValue())){
            return new NoneStepDescriptorImpl();
        }
        //logger.error("authenticationType " + authenticationType +" not supported");
        return null;
    }


    @Override
    public Provider initialize() throws IOException {
        TestContext.put("stepDescriptor",getStepDescriptor(Configuration.get("jaxrs").asMap().get("auth").toString()));
        return this;
    }

    @Override
    public void register() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public JsonNode parser(Element element) {
        return null;
    }

    @Override
    public Provider iface(Class iface) {
        return null;
    }

    @Override
    public <RespT> void exec(String name, org.apache.avro.ipc.CallFuture<RespT> callback, io.grpc.ManagedChannel channel, Object[] args, Class[] types, HashMap<String, Object> callOptions) {

    }


}