package org.softauto.discovery.plugin.api;

import java.util.List;

public interface Provider {

      List<String> getDiscoverByAnnotation();

      List<String> getUnboxReturnType();

      List<String> getUnboxEexcludeReturnType();
}
