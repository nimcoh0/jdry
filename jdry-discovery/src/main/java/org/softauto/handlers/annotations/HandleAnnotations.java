package org.softauto.handlers.annotations;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.utils.Multimap;
import soot.tagkit.*;

import java.util.*;


public class HandleAnnotations {

    private static Logger logger = LogManager.getLogger(HandleAnnotations.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    List<Tag> tags;



    public HandleAnnotations(LinkedList<Tag> tags){
        this.tags = tags;
    }

    public LinkedHashMap<String, Object> build() {
        Multimap annotations = new Multimap();
        try {
            if(tags != null && tags.size() > 0) {
                     for (Tag tag : tags) {
                       if (tag instanceof VisibilityAnnotationTag) {
                            VisibilityAnnotationTag t = (VisibilityAnnotationTag) tag;
                            if (t != null) {
                                 for (AnnotationTag annotation : t.getAnnotations()) {
                                        Multimap hm1 = new Multimap();
                                            for (AnnotationElem annotationElem : annotation.getElems()) {
                                                Multimap hm = new Multimap();
                                                Multimap n = resolve(annotationElem, hm, null);
                                                for (Map.Entry entry : n.getMap().entrySet()) {
                                                    String key = null;
                                                    if (entry.getKey()  == null) {
                                                        key = "value";
                                                    } else {
                                                        key = entry.getKey() .toString();
                                                    }
                                                    if(key.equals("type") && hm1.getMap().containsKey("type")){
                                                        hm1.getMap().remove("type");
                                                    }
                                                    hm1.put(key, entry.getValue());
                                                }
                                            }
                                            String realType = (annotation.getType().substring(1,annotation.getType().length()-1)).replace("/",".");
                                            annotations.put(realType, hm1);
                                    }
                            }
                        }
                        if (tag instanceof VisibilityParameterAnnotationTag) {
                            VisibilityParameterAnnotationTag tt = (VisibilityParameterAnnotationTag) tag;
                            if (tt != null) {
                                ArrayList<VisibilityAnnotationTag> a = tt.getVisibilityAnnotations();
                                 for(int i=0;i<a.size();i++){
                                        VisibilityAnnotationTag t = a.get(i);
                                        if(t != null)
                                            for (AnnotationTag annotation : t.getAnnotations()) {
                                                Multimap hm1 = new Multimap();
                                                for (AnnotationElem annotationElem : annotation.getElems()) {
                                                        Multimap hm = new Multimap();
                                                        Multimap n = resolve(annotationElem, hm, null);
                                                        for (Map.Entry entry : n.getMap().entrySet()) {
                                                            String key = null;
                                                            if (entry.getKey() == null) {
                                                                key = "value";
                                                            } else {
                                                                key = entry.getKey().toString();
                                                            }
                                                           if(key.equals("type") && hm1.getMap().containsKey("type")){
                                                                hm1.getMap().remove("type");
                                                           }
                                                           hm1.put(key, entry.getValue());
                                                        }
                                                    }
                                                    hm1.put("index",i,false);
                                                    String realType = (annotation.getType().substring(1,annotation.getType().length()-1)).replace("/",".");
                                                    annotations.put(realType, hm1);
                                            }
                                    }
                              }
                        }
                    }
            }
        } catch (Exception e) {
            logger.error(JDRY,"fail build annotations ",e.getMessage());
        }
        return annotations.getMap();
    }


    public  Multimap resolve(AnnotationElem annotationElem,Multimap hm,AnnotationElem parent){
        try {
            if(annotationElem instanceof AnnotationAnnotationElem){
                AnnotationTag tag = ((AnnotationAnnotationElem) annotationElem).getValue();
                Multimap hm1 = new Multimap();
                for(int i= 0;i<tag.getElems().size();i++) {
                    AnnotationElem elem = (AnnotationElem) tag.getElems().toArray()[i];
                    if(annotationElem.getName() != null){
                        parent = annotationElem;
                    }
                    Multimap n = resolve(elem,hm1,parent);

                }
                if(annotationElem.getName() != null) {
                    hm.put(annotationElem.getName(), hm1);
                }else {
                    hm.put(parent.getName(), hm1);
                }
            }else if(annotationElem instanceof AnnotationArrayElem){
                ArrayList<AnnotationElem> annotationArray = (ArrayList<AnnotationElem>) ((AnnotationArrayElem) annotationElem).getValues();
                Multimap hm1 = new Multimap();
                for(int i = 0; i < annotationArray.size(); i++) {
                    AnnotationElem elem = annotationArray.get(i);
                    if(annotationElem.getName() != null){
                        parent = annotationElem;
                    }
                    Multimap n = resolve(elem,hm1,parent);

                    hm.replace(n);
                }
            }else {
                String name = null;
                if(annotationElem.getName() == null){
                    name = parent.getName();
                }else {
                    name = annotationElem.getName();
                }
                if (annotationElem instanceof AnnotationStringElem) {
                    if(!name.equals("type")) {
                        hm.put(name, ((AnnotationStringElem) annotationElem).getValue());
                        if(!hm.getMap().containsKey("type"))
                            hm.put("type", "java.lang.String");
                    }else {
                        hm.put(name, ((AnnotationStringElem) annotationElem).getValue(),true);
                    }

                }
                if (annotationElem instanceof AnnotationClassElem) {
                    hm.put(name, ((AnnotationClassElem) annotationElem).getDesc());
                    if(!name.equals("type"))
                         hm.put("type", "java.lang.Class");
                }
                if (annotationElem instanceof AnnotationBooleanElem) {
                    hm.put(name, ((AnnotationBooleanElem) annotationElem).getValue());
                    if(!name.equals("type"))
                         hm.put("type", "java.lang.Boolean");
                }
                if (annotationElem instanceof AnnotationDoubleElem) {
                    hm.put(name, ((AnnotationDoubleElem) annotationElem).getValue());
                    if(!name.equals("type"))
                          hm.put("type", "java.lang.Double");
                }
                if (annotationElem instanceof AnnotationEnumElem) {
                    hm.put(name, ((AnnotationEnumElem) annotationElem).getConstantName());
                    if(!name.equals("type"))
                         hm.put("type", "java.lang.Enum");
                }
                if (annotationElem instanceof AnnotationFloatElem) {
                    hm.put(name, ((AnnotationFloatElem) annotationElem).getValue());
                    if(!name.equals("type"))
                         hm.put("type", "java.lang.Float");
                }
                if (annotationElem instanceof AnnotationIntElem) {
                    if(!name.equals("type")) {
                        hm.put(name, ((AnnotationIntElem) annotationElem).getValue());
                        if(!hm.getMap().containsKey("type"))
                            hm.put("type", "java.lang.Integer",true);
                    }else {
                        hm.put(name, ((AnnotationIntElem) annotationElem).getValue(),true);
                    }

                }
                if (annotationElem instanceof AnnotationLongElem) {
                    hm.put(name, ((AnnotationLongElem) annotationElem).getValue());
                    if(!name.equals("type"))
                         hm.put("type", "java.lang.Long");
                }
            }

        } catch (Exception e) {
            logger.error(JDRY,"fail resolve "+ annotationElem.getName(),e.getMessage());
        }
        return hm;
    }
}
