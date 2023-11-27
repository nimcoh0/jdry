package org.softauto.analyzer.base.source;

public interface Tree  {

    public enum Kind {

        ITEM(ItemTree.class);

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
