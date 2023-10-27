package org.softauto.analyzer.base.source;


public interface TreeVisitor<R,T,D> {

    R visitAssert(AssertTree anAssert, T t, D d, R r);

    R visitTest(TestTree test, T t, D d, R r);

    R visitData(DataTree dataTree, T t, D d, R r);

    R visitAfter(AfterTree after, T t, D d, R r);

    R visitApi(ApiTree api, T t, D d, R r);

    R visitItem(ItemTree item, T t, D d, R r);

    R visitExpected(ExpectedTree expected, T t, D d, R r);


}
