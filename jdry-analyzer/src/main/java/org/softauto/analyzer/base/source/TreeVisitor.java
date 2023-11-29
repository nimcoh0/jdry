package org.softauto.analyzer.base.source;


public interface TreeVisitor<R,T,D> {

    R visitItem(ItemTree item, T t, D d, R r);

    R visitClassType(ClassTypeTree classType, T t, D d, R r);

}
