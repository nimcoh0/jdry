package org.softauto.discovery;

import soot.SootField;
import soot.tagkit.AnnotationTag;
import soot.tagkit.VisibilityAnnotationTag;

import java.util.List;

public class DiscoveryFieldByAnnotation implements IDiscovery {


    List<Object> annotations;

    public void setAnnotations(List<Object> annotations) {
        this.annotations = annotations;
    }

    @Override
    public Object apply(Object o) {
        try {
            SootField sootField = (SootField)o;
            VisibilityAnnotationTag tag = (VisibilityAnnotationTag) sootField.getTag("VisibilityAnnotationTag");
            if (tag != null) {
                for (AnnotationTag annotation : tag.getAnnotations()) {
                    for (Object obj : annotations) {
                        if (annotation.getType().contains(obj.toString().replace(".","/"))) {
                            return sootField;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //logger.error("fail discovery for "+o.getClass().getTypeName());
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
