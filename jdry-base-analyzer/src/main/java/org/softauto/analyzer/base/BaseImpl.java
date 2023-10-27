package org.softauto.analyzer.base;

import org.softauto.analyzer.base.tree.BaseScanner;
import org.softauto.analyzer.base.tree.BaseTree;
import org.softauto.analyzer.model.Item;
import org.softauto.analyzer.model.data.Data;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.core.skeletal.tree.phase.Phase;
import org.softauto.analyzer.core.system.plugin.api.Provider;
import org.softauto.analyzer.core.utils.Analyzer;
import org.softauto.analyzer.core.utils.ResultTypeAnalyzer;

public class BaseImpl implements Provider {
    @Override
    public Analyzer getAnalyzer(Phase.kind state) {
        return null;
    }

    @Override
    public Item Analyze(GenericItem tree, Item item, Data data) {
        return (Item)  new BaseTree.Item().accept(new BaseScanner(), tree, null, null);
    }

    @Override
    public ResultTypeAnalyzer getResultTypeAnalyzer() {
        return null;
    }
}
