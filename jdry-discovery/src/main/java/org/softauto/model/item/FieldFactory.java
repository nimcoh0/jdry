package org.softauto.model.item;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.Discover;
import org.softauto.handlers.HandleFieldAnnotation;
import soot.SootField;

import java.util.HashMap;

public class FieldFactory {

    private static Logger logger = LogManager.getLogger(Discover.class);

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
            logger.error("fail build field item for "+ sootField.getName());
        }
        return this;
    }
}
