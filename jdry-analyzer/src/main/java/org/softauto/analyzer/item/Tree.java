package org.softauto.analyzer.item;

import org.softauto.analyzer.model.genericItem.GenericItem;

public interface Tree {

    TreeImpl walkOnTree(GenericItem genericItem, TreeVisitor visitor);


}
