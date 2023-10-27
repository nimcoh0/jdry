package org.softauto.discovery.handlers.method;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.Main;
import soot.SootMethod;
import soot.tagkit.AnnotationTag;
import soot.tagkit.VisibilityAnnotationTag;

import java.util.List;

/**
 * include only methods that are tag by selected annotations
 */
public class DiscoveryByAnnotation implements IDiscovery {

    private static Logger logger = LogManager.getLogger(Main.class);

    List<Object> annotations;

    public IDiscovery set(List<Object> annotations){
        this.annotations = annotations;
        return this;
    }

    @Override
    public Object apply(Object o) {
        try {
            SootMethod sootMethod = (SootMethod)o;
            VisibilityAnnotationTag tag = (VisibilityAnnotationTag) sootMethod.getTag("VisibilityAnnotationTag");
            if (tag != null) {
                for (AnnotationTag annotation : tag.getAnnotations()) {
                    for (Object obj : annotations) {
                        if (annotation.getType().contains(obj.toString().replace(".","/"))) {
                            return sootMethod;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("fail discovery for "+o.getClass().getTypeName());
        }

        return null;
    }

    @Override
    public String getName() {
        return this.getClass().getTypeName();
    }
}
