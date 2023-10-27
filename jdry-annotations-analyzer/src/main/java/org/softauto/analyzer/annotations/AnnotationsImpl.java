package org.softauto.analyzer.annotations;

import org.softauto.analyzer.annotations.tree.AnnotationsScanner;
import org.softauto.analyzer.annotations.tree.AnnotationsTree;
import org.softauto.analyzer.model.Item;
import org.softauto.analyzer.model.data.Data;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.core.skeletal.tree.phase.Phase;
import org.softauto.analyzer.core.system.plugin.api.Provider;
import org.softauto.analyzer.core.utils.Analyzer;
import org.softauto.analyzer.core.utils.ResultTypeAnalyzer;

public class AnnotationsImpl implements Provider {
    @Override
    public Analyzer getAnalyzer(Phase.kind state) {
        return null;
    }

    @Override
    public Item Analyze(GenericItem tree, Item item, Data data) {
        return new AnnotationsTree.Item().accept(new AnnotationsScanner(), tree, null, item);
    }

    @Override
    public ResultTypeAnalyzer getResultTypeAnalyzer() {
        return null;
    }
}
