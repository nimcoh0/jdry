package org.softauto.rpc.analyzer;

import org.softauto.analyzer.core.system.scanner.AbstractAnnotationScanner;
import org.softauto.analyzer.core.system.scanner.AnnotationHelper;
import org.softauto.analyzer.core.system.scanner.AnnotationScanner;
import org.softauto.analyzer.core.utils.Analyzer;
import org.softauto.analyzer.model.genericItem.GenericItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        /*
        if(AnnotationHelper.isExist("Lorg/softauto/annotations/InitializeForTesting;",tree.getAnnotations())){
            AbstractAnnotationScanner scanner = new AnnotationScanner().setPath("Lorg/softauto/annotations/InitializeForTesting;").setAnnotations(tree.getAnnotations()).scanner();
            if(scanner != null){
                tree.setClassType(org.softauto.annotations.ClassType.fromString(scanner.get("value").asString()));
                if(scanner.has("parameters")){
                    Object p =  scanner.get("parameters").asObject();
                    if(p instanceof Map){
                        //for(Map.Entry entry :  ((HashMap<String,String>)p).entrySet()){
                        tree.addConstructorParameter(((HashMap<String,String>)p).get("type"),((HashMap<String,String>)p).get("value"));
                        //}
                    }
                    if(p instanceof ArrayList){
                        for(Object o : (ArrayList)p){
                            //for(Map.Entry entry :  ((HashMap<String,String>)o).entrySet()){
                            tree.addConstructorParameter(((HashMap<String,String>)o).get("type"),((HashMap<String,String>)o).get("value"));
                            //}
                        }
                    }
                }
            }


        }

         */
        return callOption;
    }
}
