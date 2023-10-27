package org.softauto.analyzer.annotations.source;


import org.softauto.analyzer.core.skeletal.tree.phase.Phase;

public interface Tree extends Phase {



    public enum Kind {


        ASSERTS(AssertTree.class),
        PLUGIN(PluginTree.class),
        TEST(TestTree.class),
        DATA(DataTree.class),
        LISTENER(ListenerTree.class),
        AFTER(AfterTree.class),
        API(ApiTree.class),
        ITEM(ItemTree.class),
        CALLBACK(CallBackTree.class),
        EXPECTED(ExpectedTree.class),
        CLASSTYPE(ClassTypeTree.class);



        Kind(Class<? extends Tree> intf) {
            associatedInterface = intf;
        }



        public Class<? extends Tree> asInterface() {
            return associatedInterface;
        }

        private final Class<? extends Tree> associatedInterface;
    }

    Kind getKind();



    public <R,T,D> R accept(TreeVisitor<R,T,D> visitor, T t, D d, R r) ;
}
