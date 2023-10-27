package org.softauto.analyzer.base.source;


import org.softauto.analyzer.core.skeletal.tree.phase.Phase;

public interface Tree extends Phase {



    public enum Kind {


        ASSERTS(AssertTree.class),
        TEST(TestTree.class),
        DATA(DataTree.class),
        AFTER(AfterTree.class),
        API(ApiTree.class),
        ITEM(ItemTree.class),
        EXPECTED(ExpectedTree.class);




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
