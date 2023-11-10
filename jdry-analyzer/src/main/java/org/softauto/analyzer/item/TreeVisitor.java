package org.softauto.analyzer.item;

import org.softauto.analyzer.model.Item;
import org.softauto.analyzer.model.genericItem.GenericItem;

public abstract class TreeVisitor {

    public abstract Item visitBase(GenericItem genericItem);

    public abstract Item visitJdryAnnotations(Item item);

}
