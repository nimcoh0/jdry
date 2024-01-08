package org.softauto.spring.web.analyzer;

import org.softauto.analyzer.core.system.scanner.AnnotationHelper;
import org.softauto.analyzer.model.Item;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.core.system.plugin.api.Provider;
import org.softauto.analyzer.core.utils.Analyzer;
import org.softauto.spring.web.analyzer.initializers.BaseInitializer;

import java.util.*;

public class PluginFactory  implements Provider{

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

    public List<String> getApiAnnotations(){
        List<String> apiAnnotations = new ArrayList<>();
        apiAnnotations.add("org.springframework.web.bind.annotation.PostMapping");
        apiAnnotations.add("org.springframework.web.bind.annotation.DeleteMapping");
        apiAnnotations.add("org.springframework.web.bind.annotation.GetMapping");
        apiAnnotations.add("org.springframework.web.bind.annotation.PutMapping");
        apiAnnotations.add("org.springframework.web.bind.annotation.RequestMapping");
        apiAnnotations.add("org.springframework.web.bind.annotation.RestController");
        return apiAnnotations;
    }


    private Set<String> jaxrsEndPoints = Collections
            .unmodifiableSet(new HashSet<>(Arrays.asList("org.springframework.web.bind.annotation.RequestMapping","org.springframework.web.bind.annotation.PostMapping","org.springframework.web.bind.annotation.GetMapping","org.springframework.web.bind.annotation.DeleteMapping","org.springframework.web.bind.annotation.PutMapping")));


    private Object getObject(Object[] objs,String clazz){
        for(Object o : objs){
            if(o != null && o.getClass().getTypeName().equals(clazz)){
                return o;
            }
        }
        return false;
    }

    public boolean isInterest(GenericItem tree){
        if(AnnotationHelper.isContains(jaxrsEndPoints,(tree.getAnnotations()))) {
            return true;
        }
        return false;
    }




}
