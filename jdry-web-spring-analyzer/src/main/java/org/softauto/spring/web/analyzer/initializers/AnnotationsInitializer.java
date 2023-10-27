package org.softauto.spring.web.analyzer.initializers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.core.system.config.Configuration;
import org.softauto.analyzer.core.system.scanner.AbstractAnnotationScanner;
import org.softauto.analyzer.core.system.scanner.AnnotationScanner;
import org.softauto.analyzer.core.utils.Analyzer;
import org.softauto.spring.web.analyzer.PluginHelperImpl;
import org.softauto.spring.web.analyzer.handlers.HandleRequestTypeJaxrs;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class AnnotationsInitializer  implements Analyzer {

    private static Logger logger = LogManager.getLogger(AnnotationsInitializer.class);

    private GenericItem tree;

    private LinkedHashMap<String, Object> annotations;

    private String path  = "Lorg/softauto/jaxrs/annotations/JAXRS;";

    public  AnnotationsInitializer setTree(GenericItem tree) {
        this.tree = tree;
        return this;
    }

    public  AnnotationsInitializer setAnnotations(LinkedHashMap<String, Object> annotations) {
        this.annotations = annotations;
        return this;
    }

    private boolean isEndPoint(){
        if(Configuration.has("jax_rs_end_point")){
            List<String> endPointsList =   Configuration.get("jax_rs_end_point").asList();
            for(String s : endPointsList ){
                if(tree.getAnnotations().toString().contains(s)){
                    return true;
                }
            }
        }
        return false;
    }

    public   HashMap<String,Object> initialize() {
        if(isEndPoint()) {
            try {
                AbstractAnnotationScanner scanner = new AnnotationScanner().setPath(path).setAnnotations(annotations).scanner();
                if (scanner != null && scanner.getMapList() != null) {
                    HashMap<String, Object> annotationsCallOption = PluginHelperImpl.getCallOption(scanner.getMapList());
                    new HandleRequestTypeJaxrs().setTree(tree).setScanner(scanner).setCallOption(annotationsCallOption).build();
                    return annotationsCallOption;
                }
                logger.debug("successfully initialize plugin data ");
            } catch (Exception e) {
                logger.error("fail initialize ", e);
            }
        }
        return null;
    }


}
