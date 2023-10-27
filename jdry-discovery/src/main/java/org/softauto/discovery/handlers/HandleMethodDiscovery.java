package org.softauto.discovery.handlers;

import org.softauto.utils.Function;
import soot.SootMethod;

public class HandleMethodDiscovery {

    public static SootMethod handleMethodDiscovery(Function function, Object o){
        return (SootMethod) function.apply(o);
    }


}
