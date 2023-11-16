package org.softauto.filter;

import org.softauto.utils.Function;

public interface IFilter extends Function {

    IFilter set(Object var);

    Object apply(Object o);

    String getName();

}
