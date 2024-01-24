package org.softauto.model.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.Discover;
import org.softauto.handlers.annotations.HandelClassAnnotation;
import org.softauto.model.clazz.ClassFactory;
import org.softauto.model.item.Item;
import org.softauto.model.item.ItemBuilder;
import org.softauto.utils.Entity;
import soot.SootClass;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class EntityFactory {

    private static Logger logger = LogManager.getLogger(EntityFactory.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    Item item;

    Entity entity;

    public EntityFactory setEntity(Entity entity) {
        this.entity = entity;
        return this;
    }

    public Item getItem() {
        return item;
    }

    public EntityFactory build() {
        try {
            SootClass c = entity.getEntity();
            item = ItemBuilder.newBuilder().setNamespce(c.getPackageName()).setName(c.getName()).setType("entity").build().getItem();

        } catch (Exception e) {
            logger.error(JDRY,"fail build entity items");
        }
        return this;
    }
}
