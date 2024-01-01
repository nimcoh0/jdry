package org.softauto.discovery;



import org.softauto.discovery.plugin.api.Provider;

import java.util.ArrayList;
import java.util.List;

public class PluginRpcImpl implements Provider {

    @Override
    public List<String> getDiscoverByAnnotation() {
            List<String> discoverAnnotations = new ArrayList<>();
            discoverAnnotations.add("org.softauto.annotations.ApiForTesting");
            discoverAnnotations.add("org.softauto.annotations.ListenerForTesting");
            discoverAnnotations.add("org.softauto.annotations.InitializeForTesting");
            return discoverAnnotations;
    }

}
