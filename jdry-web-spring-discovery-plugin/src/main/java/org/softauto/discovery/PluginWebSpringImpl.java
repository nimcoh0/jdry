package org.softauto.discovery;


import org.softauto.discovery.plugin.api.Provider;

import java.util.ArrayList;
import java.util.List;

public class PluginWebSpringImpl implements Provider {

    @Override
    public List<String> getDiscoverByAnnotation() {
            List<String> discoverAnnotations = new ArrayList<>();
            discoverAnnotations.add("org.springframework.web.bind.annotation.PostMapping");
            discoverAnnotations.add("org.springframework.web.bind.annotation.DeleteMapping");
            discoverAnnotations.add("org.springframework.web.bind.annotation.GetMapping");
            discoverAnnotations.add("org.springframework.web.bind.annotation.PutMapping");
            discoverAnnotations.add("org.springframework.web.bind.annotation.RequestMapping");
            discoverAnnotations.add("org.springframework.web.bind.annotation.RestController");
            return discoverAnnotations;
    }

    @Override
    public List<String> getUnboxReturnType(){
        List<String> unboxReturnType = new ArrayList<>();
        unboxReturnType.add("org.springframework.http.ResponseEntity");
        return unboxReturnType;
    }

    @Override
    public List<String> getUnboxEexcludeReturnType(){
        List<String> unboxEexcludeReturnType = new ArrayList<>();
        unboxEexcludeReturnType.add("java.lang.Exception");
        return unboxEexcludeReturnType;
    }

}
