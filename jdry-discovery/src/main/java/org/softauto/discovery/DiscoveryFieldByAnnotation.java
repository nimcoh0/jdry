package org.softauto.discovery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.Discover;
import soot.SootField;
import soot.tagkit.AnnotationTag;
import soot.tagkit.VisibilityAnnotationTag;

import java.util.List;

public class DiscoveryFieldByAnnotation implements IDiscovery {


    List<Object> annotations;

    public void setAnnotations(List<Object> annotations) {
        this.annotations = annotations;
    }

    private static Logger logger = LogManager.getLogger(Discover.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    @Override
    public Object apply(Object o) {
        try {
            SootField sootField = (SootField)o;
            VisibilityAnnotationTag tag = (VisibilityAnnotationTag) sootField.getTag("VisibilityAnnotationTag");
            if (tag != null) {
                for (AnnotationTag annotation : tag.getAnnotations()) {
                    if (annotations != null && annotations.size() > 0) {
                        for (Object obj : annotations) {
                            if (annotation.getType().contains(obj.toString().replace(".", "/"))) {
                                return sootField;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(JDRY,"fail discovery for "+o.getClass().getTypeName());
        }

        return null;
    }

    @Override
    public IDiscovery set(List<Object> vars) {
        this.annotations = vars;
        return this;
    }

    @Override
    public String getName() {
        return null;
    }
}
