package org.softauto.analyzer.item;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.model.genericItem.GenericItem;

public class TreeImpl implements Tree {

    private static Logger logger = LogManager.getLogger(TreeImpl.class);

    @Override
    public TreeImpl walkOnTree(GenericItem tree, TreeVisitor visitor) {
        visitor.visitBase(tree);
        return this;
    }



}
