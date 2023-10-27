package org.softauto.model.item;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.Main;
import org.softauto.discovery.handlers.flow.FlowObject;
import org.softauto.handlers.HandelClassAnnotation;
import org.softauto.handlers.HandelConstructorAnnotation;
import org.softauto.handlers.HandleAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;


public class ItemFactory {

    private static Logger logger = LogManager.getLogger(Main.class);

    Item item;

    public FlowObject flowObject;

    public Item getItem() {
        return item;
    }

    public ItemFactory setFlowObject(FlowObject flowObject) {
        this.flowObject = flowObject;
        return this;
    }

    public ItemFactory build(){
        try{
            HashMap<String, Object> annotations = new HashMap<>();
            HashMap<String, Object> classAnnotations = new HandelClassAnnotation(flowObject.getMethod().getDeclaringClass()).analyze();
            if(flowObject.getMethod().getName().equals("<init>")) {
                HashMap<String, Object> annotations1 = new HandelConstructorAnnotation(flowObject.getMethod().getDeclaringClass()).analyze();
                annotations.put("constructor",annotations1);
            }else {
                LinkedList l = (LinkedList<HashMap<?, ?>>) ((ArrayList)flowObject.getMethod().getTags()).stream().collect(Collectors.toCollection(LinkedList::new));
                annotations = new HandleAnnotations(l).build();
            }
            if(classAnnotations != null && classAnnotations.size() > 0) {
                annotations.put("class",classAnnotations);
            }
            item = ItemBuilder.newBuilder()
                    .setName(flowObject.getName())
                    .setType("method")
                    .setNamespce(flowObject.getClazz())
                    .setParametersTypes(flowObject.getMethod().getParameterTypes())
                    .setArgumentsNames(flowObject.getArgsname())
                    .setReturnType(flowObject.getReturnType())
                    .setId(flowObject.getMethod().getNumber())
                    .setChildes(flowObject.getChileds())
                    .setAnnotations(annotations)
                    .setClassInfo(flowObject.getClassInfo())

                    .setCrudToSubject(flowObject.getCrudToSubject())
                    .build()
                    .getItem();



        }catch (Exception e){
            logger.error("fail build item ",e.getMessage());
        }
        return this;
    }



}
