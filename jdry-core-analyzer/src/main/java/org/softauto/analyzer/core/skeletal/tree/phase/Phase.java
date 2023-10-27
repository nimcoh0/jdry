package org.softauto.analyzer.core.skeletal.tree.phase;

public interface Phase {

    public enum kind {
        BASE(BaseTree.class),
        ANNOTATIONS(AnnotationsTree.class),
        NAMING(Naming.class),
        META(MetaTree.class),
        DGTREE(DgTree.class),
        DRTREE(DrTree.class),
        JDRYNONETREE(JdryNoneTree.class),
        JDRYTREE(JdryTree.class);

        kind(Class<? extends Phase> intf) {
            associatedInterface = intf;
        }

        public Class<? extends Phase> asInterface() {
            return associatedInterface;
        }

        private final Class<? extends Phase> associatedInterface;
    }

    kind getPhaseKind();
}
