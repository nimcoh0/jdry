package org.softauto.transformers;

import soot.Body;
import soot.BodyTransformer;

import java.util.Map;

public class VNTransformer1 extends BodyTransformer {

    @Override
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        b.getLocals();
    }
}
