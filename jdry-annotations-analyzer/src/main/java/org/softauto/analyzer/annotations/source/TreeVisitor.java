package org.softauto.analyzer.annotations.source;


public interface TreeVisitor<R,T,D> {

    R visitListener(ListenerTree listener, T t, D d, R r);

    R visitItem(ItemTree item, T t, D d, R r);

    
}
