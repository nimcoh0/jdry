package org.softauto.handlers.annotations;

import soot.SootField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class HandleFieldAnnotation {

    SootField sootField ;


    public HandleFieldAnnotation(SootField sootField){
        this.sootField = sootField;
    }


    public HashMap<String, Object> analyze(){
        if(sootField.getTags() != null && sootField.getTags().size() > 0) {
            LinkedList l = (LinkedList<HashMap<?, ?>>) ((ArrayList) sootField.getTags()).stream().collect(Collectors.toCollection(LinkedList::new));
            return new HandleAnnotations(l).build();
        }
        return new HashMap<>();
    }
}
