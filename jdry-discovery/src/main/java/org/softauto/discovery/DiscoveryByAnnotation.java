package org.softauto.discovery;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.Discover;
import soot.SootMethod;
import soot.tagkit.AnnotationTag;
import soot.tagkit.VisibilityAnnotationTag;

import java.util.List;

/**
 * include only methods that are tag by selected annotations
 */
public class DiscoveryByAnnotation implements IDiscovery {

    private static Logger logger = LogManager.getLogger(Discover.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

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

            if (annotations != null && annotations.size() > 0 ) {
                if (tag != null) {
                    for (AnnotationTag annotation : tag.getAnnotations()) {
                        for (Object obj : annotations) {
                            if (annotation.getType().contains("L" + obj.toString().replace(".", "/") + ";")) {
                                return sootMethod;
                            }
                        }
                    }
                }
            }else {
                return sootMethod;
            }


        } catch (Exception e) {
            logger.error(JDRY,"fail discovery for "+o.getClass().getTypeName());
        }

        return (SootMethod)o;
    }

    @Override
    public String getName() {
        return this.getClass().getTypeName();
    }
}
