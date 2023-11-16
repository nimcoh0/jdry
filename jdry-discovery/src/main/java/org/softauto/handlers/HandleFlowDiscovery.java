package org.softauto.handlers;

import org.softauto.flow.FlowObject;
import org.softauto.utils.Function;

public class HandleFlowDiscovery {

    public static FlowObject handleFlowDiscovery(Function function, Object o){
        return (FlowObject) function.apply(o);
    }
}
