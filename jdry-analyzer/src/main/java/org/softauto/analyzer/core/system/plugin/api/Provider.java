package org.softauto.analyzer.core.system.plugin.api;


import org.softauto.analyzer.core.utils.Analyzer;
import org.softauto.analyzer.model.Item;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.core.IStepDescriptor;

import java.util.HashMap;

public interface Provider {

      Analyzer getAnalyzer(GenericItem tree);

      Item Analyze(GenericItem tree, Item item);

      boolean isInterest(GenericItem tree);


}
