package org.softauto.analyzer.base.tree;


import org.softauto.analyzer.base.source.*;

public class BaseScanner<R,T,D>  implements TreeVisitor<R,T,D> {


    public R scan(Tree tree, T t, D d, R r) {
        return (tree == null) ? null : tree.accept(this, t, d,r);
    }

    public R scanAndReduce(Tree node, T t, D d, R r) {
        return reduce(scan(node,t, d,r), r);
    }

    public R scan(Iterable<? extends Tree> nodes, T t, D d, R r) {
        if (nodes != null) {
            boolean first = true;
            for (Tree node : nodes) {
                r = (first ? scan(node,t, d, r) : scanAndReduce(node,t, d, r));
                first = false;
            }
        }
        return r;
    }


    private R scanAndReduce(Iterable<? extends Tree> nodes, T t, D d, R r) {
        return reduce(scan(nodes,t, d,r), r);
    }

    public R reduce(R r1, R r2) {
        return r1;
    }



    @Override
    public R visitItem(ItemTree item, T t, D d, R r) {
       return r;
    }
}
