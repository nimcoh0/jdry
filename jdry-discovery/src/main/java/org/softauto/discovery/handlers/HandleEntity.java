package org.softauto.discovery.handlers;

import org.softauto.utils.ApplyRule;

public class HandleEntity {

    public static boolean isEntity(String type){
        return (boolean) ApplyRule.setRule("entity_identify").addContext("type", type).apply().getResult();
    }

}
