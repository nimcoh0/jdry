package org.softauto.rpc.analyzer;

import org.softauto.analyzer.core.utils.Analyzer;
import org.softauto.analyzer.model.genericItem.GenericItem;
import java.util.HashMap;


public class Initializer implements Analyzer {

    GenericItem tree;

    public GenericItem getTree() {
        return tree;
    }

    public Initializer setTree(GenericItem tree) {
        this.tree = tree;
        return this;
    }

    @Override
    public HashMap<String, Object> initialize() {
        HashMap<String,Object> callOption = new HashMap<>();
        callOption.put("protocol", "RPC");
        return callOption;
    }
}
