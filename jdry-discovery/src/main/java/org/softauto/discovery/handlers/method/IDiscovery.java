package org.softauto.discovery.handlers.method;

import org.softauto.utils.Function;

import java.util.List;

public interface IDiscovery extends Function {

    Object apply(Object o);

    IDiscovery set(List<Object> vars);

    String getName();

}
