package org.softauto.handlers;

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
        LinkedList l = (LinkedList<HashMap<?, ?>>) ((ArrayList)sootField.getTags()).stream().collect(Collectors.toCollection(LinkedList::new));
        return  new HandleAnnotations(l).build();
    }
}
