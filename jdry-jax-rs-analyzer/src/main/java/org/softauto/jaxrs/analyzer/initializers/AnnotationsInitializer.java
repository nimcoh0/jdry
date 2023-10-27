package org.softauto.jaxrs.analyzer.initializers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.core.system.scanner.AbstractAnnotationScanner;
import org.softauto.analyzer.core.system.scanner.AnnotationScanner;
import org.softauto.analyzer.core.utils.Analyzer;
import org.softauto.jaxrs.analyzer.PluginHelperImpl;
import org.softauto.jaxrs.analyzer.handlers.HandleRequestTypeJaxrs;

import java.util.LinkedHashMap;

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
        if(tree.getAnnotations().toString().contains("/ws/rs/POST;") || tree.getAnnotations().toString().contains("/ws/rs/GET;") || tree.getAnnotations().toString().contains("/ws/rs/DELETE;") || tree.getAnnotations().toString().contains("/ws/rs/PUT;")){
            return true;
        }
        return false;
    }

    private boolean isController(){
        if(tree.getAnnotations().toString().contains("/ws/rs/PATH;") ){
            return true;
        }
        return false;
    }

    public  LinkedHashMap<String,Object> initialize() {
        if(isEndPoint()) {
            try {
                AbstractAnnotationScanner scanner = new AnnotationScanner().setPath(path).setAnnotations(annotations).scanner();
                if (scanner != null && scanner.getMapList() != null) {
                    LinkedHashMap<String, Object> annotationsCallOption = PluginHelperImpl.getCallOption(scanner.getMapList());
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
