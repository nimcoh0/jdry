package org.softauto.rpc.analyzer;

import org.softauto.analyzer.core.system.config.Configuration;
import org.softauto.analyzer.core.system.plugin.api.Provider;
import org.softauto.analyzer.core.system.scanner.AbstractAnnotationScanner;
import org.softauto.analyzer.core.system.scanner.AnnotationHelper;
import org.softauto.analyzer.core.system.scanner.AnnotationScanner;
import org.softauto.analyzer.core.utils.Analyzer;
import org.softauto.analyzer.model.Item;
import org.softauto.analyzer.model.genericItem.GenericItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PluginImpl implements Provider {
    @Override
    public Analyzer getAnalyzer(GenericItem tree) {
        return new Initializer().setTree(tree);
    }

    @Override
    public Item Analyze(GenericItem tree, Item item) {
        return null;
    }

    @Override
    public boolean isInterest(GenericItem tree) {
            if(AnnotationHelper.isExist("Lorg/softauto/annotations/ApiForTesting;",tree.getAnnotations(),"protocol")){
                AbstractAnnotationScanner scanner = new AnnotationScanner().setPath("Lorg/softauto/annotations/ApiForTesting;").setAnnotations(tree.getAnnotations()).scanner();
                if(scanner != null){
                    if(scanner.get("protocol").asString().equals("RPC")){
                        return true;
                    }
                }
            }
        return false;
    }
}
