package org.softauto.model.clazz;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.Discover;
import org.softauto.handlers.annotations.HandelClassAnnotation;
import org.softauto.model.item.Item;
import org.softauto.model.item.ItemBuilder;
import soot.SootClass;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class ClassFactory {

    private static Logger logger = LogManager.getLogger(Discover.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    Item item;

    SootClass root;

    public ClassFactory setRoot(SootClass root) {
        this.root = root;
        return this;
    }

    LinkedList<SootClass> sootClassList = new LinkedList<>();

    public ClassFactory setSootClass(LinkedList<SootClass> sootClassList) {
        this.sootClassList = sootClassList;
        return this;
    }

    public ClassFactory addSootClass(SootClass sootClass) {
        this.sootClassList.add(sootClass);
        return this;
    }

    public Item getItem() {
        return item;
    }

    public ClassFactory build() {
        try {
            LinkedHashMap<String, Object> annotationsList = new LinkedHashMap();
            for(SootClass sootClass :sootClassList){
                HashMap<String, Object> annotations = new HandelClassAnnotation(sootClass).analyze();
                annotationsList.put(sootClass.getName(),annotations);
            }
            item = ItemBuilder.newBuilder().setNamespce(root.getPackageName()).setName(root.getName()).setAnnotations(annotationsList).setType("clazz").build().getItem();

        } catch (Exception e) {
            logger.error(JDRY,"fail build class items");
        }
        return this;
    }

}
