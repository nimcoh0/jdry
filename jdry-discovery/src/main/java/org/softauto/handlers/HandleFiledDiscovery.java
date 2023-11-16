package org.softauto.handlers;

import org.softauto.utils.Function;
import soot.SootField;

public class HandleFiledDiscovery {

    public static SootField filedDiscovery(Function function, Object o){
        return (SootField) function.apply(o);
    }
}
