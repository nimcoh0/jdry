package org.softauto.analyzer.annotations.source;


public interface TreeVisitor<R,T,D> {

    R visitAssert(AssertTree anAssert, T t, D d, R r);

    R visitTest(TestTree test, T t, D d, R r);

    R visitData(DataTree dataTree, T t, D d, R r);

    R visitListener(ListenerTree listener, T t, D d, R r);

    R visitAfter(AfterTree after, T t, D d, R r);

    R visitApi(ApiTree api, T t, D d, R r);

    R visitPlugin(PluginTree plugin, T t, D d, R r);

    R visitClassType(ClassTypeTree classType, T t, D d, R r);

    R visitItem(ItemTree item, T t, D d, R r);

    R visitCallBack(CallBackTree callBack, T t, D d, R r);

    
}
