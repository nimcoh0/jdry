package org.softauto.model.field;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.Discover;
import org.softauto.handlers.annotations.HandleFieldAnnotation;
import org.softauto.model.item.Item;
import org.softauto.model.item.ItemBuilder;
import soot.SootField;

import java.util.HashMap;

public class FieldFactory {

    private static Logger logger = LogManager.getLogger(Discover.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    Item item;

    SootField sootField;

    public FieldFactory setSootField(SootField sootField) {
        this.sootField = sootField;
        return this;
    }

    public Item getItem() {
        return item;
    }

    public FieldFactory build() {
        try {
            HashMap<String, Object> annotations = new HandleFieldAnnotation(sootField).analyze();
            item = ItemBuilder.newBuilder().setAnnotations(annotations)
                    .setNamespce(sootField.getDeclaringClass().getName())
                    .setName(sootField.getName())
                    .setType("field")
                    .setModifier(sootField.getModifiers())
                    .setId(sootField.getNumber())
                    .setReturnType(sootField.getType().toString())
                    .build()
                    .getItem();
        } catch (Exception e) {
            logger.error(JDRY,"fail build field item for "+ sootField.getName());
        }
        return this;
    }
}
