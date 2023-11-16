package org.softauto.filter;

import soot.SootClass;

/**
 * include only methods that are in the app domain
 */
public class FilterByDomain implements IFilter {

    Object domain;

    public IFilter set(Object domain){
        this.domain = domain;
        return this;
    }



    @Override
    public Object apply(Object o) {
        if(((SootClass)o).getType().toString().contains(domain.toString())){
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return this.getClass().getTypeName();
    }
}
