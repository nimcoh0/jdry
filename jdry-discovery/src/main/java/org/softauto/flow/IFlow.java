package org.softauto.flow;

import org.softauto.filter.IFilter;
import org.softauto.utils.Function;
import soot.jimple.toolkits.callgraph.CallGraph;

public interface IFlow extends Function {

    IFlow setFilter(IFilter filter);

    IFlow setCallGraph(CallGraph cg);

    Object apply(Object o);

    String getName();

}
