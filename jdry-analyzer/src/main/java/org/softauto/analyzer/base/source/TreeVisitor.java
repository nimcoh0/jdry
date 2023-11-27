package org.softauto.analyzer.base.source;


public interface TreeVisitor<R,T,D> {

    R visitItem(ItemTree item, T t, D d, R r);

}
