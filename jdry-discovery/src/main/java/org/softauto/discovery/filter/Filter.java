package org.softauto.discovery.filter;

import org.softauto.utils.Function;

public class Filter {

    public static boolean filter(Function function,Object o){
        return (boolean) function.apply(o);
    }
}
