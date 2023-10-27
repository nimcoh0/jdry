package org.softauto.analyzer.core.rules.protocol.rpc;

import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.core.rules.protocol.Iprotocol;

public class Rpc implements Iprotocol {

    public String getResultType(GenericItem genericItem){
        return genericItem.getReturnType();
    }
}
