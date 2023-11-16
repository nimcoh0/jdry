package org.softauto.handlers.annotations;

import soot.SootClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class HandelClassAnnotation {

    SootClass sootClass ;


    public HandelClassAnnotation(SootClass sootClass){
        this.sootClass = sootClass;
    }


    public LinkedHashMap<String, Object> analyze(){
        LinkedList l = (LinkedList<HashMap<?, ?>>) ((ArrayList)sootClass.getTags()).stream().collect(Collectors.toCollection(LinkedList::new));
        return  new HandleAnnotations(l).build();
    }

}
