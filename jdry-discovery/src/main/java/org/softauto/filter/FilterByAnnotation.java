package org.softauto.filter;

import org.softauto.config.Configuration;
import org.softauto.config.Context;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.tagkit.AnnotationTag;
import soot.tagkit.VisibilityAnnotationTag;

import java.util.List;

public class FilterByAnnotation implements IFilter{

    List<String> annotations = Configuration.get(Context.DISCOVER_BY_ANNOTATION).asList();


    @Override
    public IFilter set(Object var) {
        return null;
    }

    @Override
    public Object apply(Object o) {
        VisibilityAnnotationTag t = null;
        if(o instanceof SootMethod){
            t =(VisibilityAnnotationTag) ((SootMethod)o).getTag("VisibilityAnnotationTag");
        }
        if(o instanceof SootField){
            t =(VisibilityAnnotationTag) ((SootField)o).getTag("VisibilityAnnotationTag");
        }
        if(o instanceof SootClass){
            t =(VisibilityAnnotationTag) ((SootClass)o).getTag("VisibilityAnnotationTag");
        }
        if(t != null) {
            for (AnnotationTag annotation : t.getAnnotations()) {
                for (String interestAnnotation : annotations) {
                    if (annotation.getType().equals((("L" + interestAnnotation + ";").replace(".", "/")))) {
                        return true;
                    }
                }
            }
        }
           return false;
    }

    @Override
    public String getName() {
        return this.getClass().getTypeName();
    }
}
