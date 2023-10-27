package org.softauto.discovery.handlers.flow;

import org.softauto.discovery.filter.IFilter;
import org.softauto.utils.Function;
import soot.jimple.toolkits.callgraph.CallGraph;

public interface IFlow extends Function {

    IFlow setFilter(IFilter filter);

    IFlow setCallGraph(CallGraph cg);

    Object apply(Object o);

    String getName();

}
