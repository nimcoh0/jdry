package org.softauto.analyzer.base;

import org.softauto.analyzer.base.tree.BaseScanner;
import org.softauto.analyzer.base.tree.BaseTree;
import org.softauto.analyzer.model.Item;
import org.softauto.analyzer.model.genericItem.GenericItem;

public class BaseImpl  {

    public Item Analyze(GenericItem tree, Item item) {
        return (Item)  new BaseTree.Item().accept(new BaseScanner(), tree, null, null);
    }


}
