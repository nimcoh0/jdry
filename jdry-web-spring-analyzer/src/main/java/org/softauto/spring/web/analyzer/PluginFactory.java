package org.softauto.spring.web.analyzer;

import org.softauto.analyzer.model.Item;
import org.softauto.analyzer.model.data.Data;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.model.test.Test;
import org.softauto.analyzer.core.system.plugin.api.Provider;
import org.softauto.analyzer.core.skeletal.tree.phase.Phase;
import org.softauto.analyzer.core.utils.Analyzer;
import org.softauto.analyzer.core.utils.ResultTypeAnalyzer;
import org.softauto.spring.web.analyzer.analyzers.AnalyzeResult;
import org.softauto.spring.web.analyzer.initializers.AnnotationsInitializer;
import org.softauto.spring.web.analyzer.initializers.BaseInitializer;
import org.softauto.spring.web.analyzer.initializers.DataInitializer;
import org.softauto.spring.web.analyzer.initializers.NoneTestInitializer;

import java.util.LinkedHashMap;

public class PluginFactory  implements Provider{

    private Object[] objs;

    public PluginFactory(Object...objs){
        this.objs = objs;
    }

    public Analyzer getAnalyzer(Phase.kind state){
        if(Phase.kind.BASE.equals(state)) {
            return new BaseInitializer().setProvider(this).setTree((GenericItem)getObject(objs,GenericItem.class.getTypeName()));
        }
        if(Phase.kind.DRTREE.equals(state)) {
            return new DataInitializer().setData((Data)getObject(objs,Data.class.getTypeName())).setTest((Test)getObject(objs,Test.class.getTypeName()));
        }
        if(Phase.kind.ANNOTATIONS.equals(state)) {
            return new AnnotationsInitializer().setTree((GenericItem)getObject(objs,GenericItem.class.getTypeName())).setAnnotations((LinkedHashMap)getObject(objs,LinkedHashMap.class.getTypeName()));
        }
        if(Phase.kind.JDRYNONETREE.equals(state)) {
            return new NoneTestInitializer().setProvider(this).setTree((GenericItem)getObject(objs,GenericItem.class.getTypeName()));
        }
        return null;
    }

    @Override
    public Item Analyze(GenericItem tree, Item item, Data data) {
        return null;
    }

    @Override
    public ResultTypeAnalyzer getResultTypeAnalyzer() {
       if(isObjectExist(objs, GenericItem.class.getTypeName())){
           return new AnalyzeResult((GenericItem)getObject(objs,GenericItem.class.getTypeName()));
       }
       return null;
    }


    private boolean isObjectExist(Object[] objs,String clazz){
        for(Object o : objs){
            if(o.getClass().getTypeName().equals(clazz)){
                return true;
            }
        }
        return false;
    }

    private Object getObject(Object[] objs,String clazz){
        for(Object o : objs){
            if(o != null && o.getClass().getTypeName().equals(clazz)){
                return o;
            }
        }
        return false;
    }




}
