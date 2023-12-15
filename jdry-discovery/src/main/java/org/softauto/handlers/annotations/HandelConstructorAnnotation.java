package org.softauto.handlers.annotations;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.Discover;
import org.softauto.utils.Multimap;
import soot.SootClass;
import soot.SootMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class HandelConstructorAnnotation {

    private static Logger logger = LogManager.getLogger(Discover.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    SootClass sootClass ;


    public HandelConstructorAnnotation(SootClass sootClass){
        this.sootClass = sootClass;
    }


    public HashMap<String, Object> analyze(){
        Multimap annotations = new Multimap();
        try {
            for(SootMethod method : sootClass.getMethods()){
                if(method.isConstructor()){
                    LinkedList l = (LinkedList<HashMap<?, ?>>) ((ArrayList)method.getTags()).stream().collect(Collectors.toCollection(LinkedList::new));
                    LinkedHashMap<String, Object> hm =  new HandleAnnotations(l).build();
                    if(hm != null && hm.size() > 0)
                        annotations.putAll(hm);
                }
            }
            logger.debug(JDRY,"successfully analyze Constructor Annotation for " +sootClass.getName());
        } catch (Exception e) {
            logger.error(JDRY,"fail analyze Constructor Annotation",e.getMessage());
        }
        return annotations.getMap();
    }

}
