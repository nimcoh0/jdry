package org.softauto.discovery.handlers;

import org.softauto.discovery.handlers.flow.FlowObject;
import org.softauto.utils.Function;

public class HandleFlowDiscovery {

    public static FlowObject handleFlowDiscovery(Function function, Object o){
        return (FlowObject) function.apply(o);
    }
}
