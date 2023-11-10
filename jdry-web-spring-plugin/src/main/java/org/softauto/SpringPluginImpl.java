package org.softauto;

import com.fasterxml.jackson.databind.JsonNode;
import io.grpc.ManagedChannel;
import org.softauto.analyzer.core.skeletal.tree.phase.Phase;
import org.softauto.analyzer.core.utils.Analyzer;
import org.softauto.analyzer.core.utils.ResultTypeAnalyzer;
import org.softauto.analyzer.model.Item;
import org.softauto.analyzer.model.data.Data;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.auth.jwt.JwtStepDescriptorImpl;
import org.softauto.core.Configuration;
import org.softauto.core.TestContext;
import org.softauto.jaxrs.AbstractStepDescriptorImpl;
import org.softauto.jaxrs.annotations.AuthenticationType;
import org.softauto.plugin.api.Provider;
import javax.lang.model.element.Element;
import java.io.IOException;
import java.util.HashMap;

public class SpringPluginImpl implements Provider {

    public AbstractStepDescriptorImpl getStepDescriptor(String authenticationType){
        if(authenticationType.equals(AuthenticationType.JWT.getValue())){
            return new JwtStepDescriptorImpl();
        }

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
    public <RespT> Object exec(String name, ManagedChannel channel, Object[] args, Class[] types, HashMap<String, Object> callOptions,String scenarioId) {
        return null;
    }

    @Override
    public <RespT> void exec(String name, org.apache.avro.ipc.Callback<RespT> callback, ManagedChannel channel, Object[] args, Class[] types, HashMap<String, Object> callOptions,String scenarioId) {

    }



}
