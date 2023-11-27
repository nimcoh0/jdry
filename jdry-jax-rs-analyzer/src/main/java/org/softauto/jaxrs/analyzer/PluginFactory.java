package org.softauto.jaxrs.analyzer;


import org.softauto.analyzer.core.system.plugin.api.Provider;
import org.softauto.analyzer.model.Item;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.core.utils.Analyzer;
import org.softauto.jaxrs.analyzer.initializers.BaseInitializer;

public class PluginFactory<R>  implements Provider {

    private Object[] objs;

    public PluginFactory(Object...objs){
        this.objs = objs;
    }

    public Analyzer getAnalyzer(GenericItem tree){
         return new BaseInitializer().setProvider(this).setTree(tree);
    }

    @Override
    public Item Analyze(GenericItem tree, Item item) {
        return null;
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
