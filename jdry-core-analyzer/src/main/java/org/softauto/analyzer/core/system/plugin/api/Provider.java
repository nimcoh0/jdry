package org.softauto.analyzer.core.system.plugin.api;


import org.softauto.analyzer.model.Item;
import org.softauto.analyzer.model.data.Data;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.core.skeletal.tree.phase.Phase;
import org.softauto.analyzer.core.utils.Analyzer;
import org.softauto.analyzer.core.utils.ResultTypeAnalyzer;

public interface Provider {

      Analyzer getAnalyzer(Phase.kind state);

      Item Analyze(GenericItem tree,Item item, Data data);

      ResultTypeAnalyzer getResultTypeAnalyzer();



}
