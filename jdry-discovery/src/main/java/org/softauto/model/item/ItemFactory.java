package org.softauto.model.item;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.Discover;
import org.softauto.flow.FlowObject;
import org.softauto.handlers.annotations.HandelClassAnnotation;
import org.softauto.handlers.annotations.HandelConstructorAnnotation;
import org.softauto.handlers.annotations.HandleAnnotations;
import soot.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;


public class ItemFactory {

    private static Logger logger = LogManager.getLogger(Discover.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

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
            }else if(flowObject.getMethod().getTags().size() > 0){
                LinkedList l = (LinkedList<HashMap<?, ?>>) ((ArrayList)flowObject.getMethod().getTags()).stream().collect(Collectors.toCollection(LinkedList::new));
                annotations = new HandleAnnotations(l).build();
            }
            if(classAnnotations != null && classAnnotations.size() > 0) {
                annotations.put("class",classAnnotations);
            }
            String type = "method";
            for(Map.Entry entry : annotations.entrySet()){
                if(entry.getKey().toString().contains("ListenerForTesting")){
                    type = "listener";
                }
            }

            item = ItemBuilder.newBuilder()
                    .setName(flowObject.getName())
                    .setType(type)
                    .setNamespce(flowObject.getClazz())
                    .setParametersTypes(flowObject.getParametersType())
                    .setArgumentsNames(flowObject.getArgsname())
                    .setReturnType(flowObject.getReturnType())
                    .setId(flowObject.getMethod().getNumber())
                    .setChildes(flowObject.getChileds())
                    .setAnnotations(annotations)
                    //.setUnboxReturnTypeTargetObject(flowObject.getUnboxReturnTypeTargetObject())
                    //.setUnboxReturnType(flowObject.getUnboxReturnType())
                    .setResponseChain(flowObject.getResponseChain())
                    .setArgsType(flowObject.getArgsType())
                    .setReturnTypeGeneric(flowObject.isReturnTypeGeneric())
                    //.setReturnName(flowObject.getReturnName())
                    .setSubsignature(flowObject.getSubsignature())
                    //.setResultParameterizedType(flowObject.getResultParameterizedType())
                    .setParametersParameterizedType(flowObject.getParametersParameterizedType())
                    .build()
                    .getItem();



        }catch (Exception e){
            logger.error(JDRY,"fail build item ",e.getMessage());
        }
        return this;
    }



}
