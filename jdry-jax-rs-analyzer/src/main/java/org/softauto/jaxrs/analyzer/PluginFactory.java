package org.softauto.jaxrs.analyzer;


import org.softauto.analyzer.core.system.config.Configuration;
import org.softauto.analyzer.core.system.plugin.api.Provider;
import org.softauto.analyzer.core.system.scanner.AnnotationHelper;
import org.softauto.analyzer.model.Item;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.core.utils.Analyzer;
import org.softauto.jaxrs.analyzer.initializers.BaseInitializer;

import java.util.*;
import java.util.stream.Collectors;

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


    private Set<String> jaxrsEndPoints = Collections
            .unmodifiableSet(new HashSet<>(Arrays.asList("/ws/rs/POST","/ws/rs/GET","/ws/rs/DELETE","/ws/rs/PUT")));

    private Object getObject(Object[] objs,String clazz){
        for(Object o : objs){
            if(o != null && o.getClass().getTypeName().equals(clazz)){
                return o;
            }
        }
        return false;
    }

    public boolean isInterest(GenericItem tree){
        //List<String> l = Configuration.get("jax_rs_end_point").asList();
        //LinkedList<String> pathList = (LinkedList<String>) ((ArrayList)l).stream().collect(Collectors.toCollection(LinkedList::new));

        if(AnnotationHelper.isContains(jaxrsEndPoints,(tree.getAnnotations()))) {
            return true;
        }
        return false;
    }

}
