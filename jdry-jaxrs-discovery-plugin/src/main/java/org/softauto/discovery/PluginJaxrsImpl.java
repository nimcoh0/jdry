package org.softauto.discovery;



import org.softauto.discovery.plugin.api.Provider;

import java.util.ArrayList;
import java.util.List;

public class PluginJaxrsImpl implements Provider {

    @Override
    public List<String> getDiscoverByAnnotation() {
        List<String> discoverAnnotations = new ArrayList<>();
        discoverAnnotations.add("javax.ws.rs.POST");
        discoverAnnotations.add("javax.ws.rs.GET");
        discoverAnnotations.add("javax.ws.rs.DELETE");
        discoverAnnotations.add("javax.ws.rs.PUT");
        discoverAnnotations.add("javax.ws.rs.Consumes");
        discoverAnnotations.add("javax.ws.rs.Produces");
        discoverAnnotations.add("javax.ws.rs.Path");
        return discoverAnnotations;
    }

    @Override
    public List<String> getUnboxReturnType(){
        List<String> unboxReturnType = new ArrayList<>();
        unboxReturnType.add("javax.ws.rs.core.Response");
        unboxReturnType.add("javax.ws.rs.core.Response$Status");
        return unboxReturnType;
    }

    @Override
    public List<String> getUnboxEexcludeReturnType(){
        List<String> unboxEexcludeReturnType = new ArrayList<>();
        unboxEexcludeReturnType.add("java.lang.Exception");
        return unboxEexcludeReturnType;
    }

}
