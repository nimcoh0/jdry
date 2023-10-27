package org.softauto.spring.web.analyzer.handlers;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.core.system.scanner.AbstractAnnotationScanner;
import org.softauto.analyzer.core.system.scanner.AnnotationScanner;

import java.util.*;

public class HandleRequestType {

    private static Logger logger = LogManager.getLogger(HandleRequestType.class);

    private LinkedHashMap<String,Object> argumentsRequestType = new LinkedHashMap();

    private GenericItem tree;

    private HashMap<String,Object> callOption;

    public HandleRequestType(GenericItem tree){
        this.tree = tree;
    }

    public HandleRequestType setCallOption(HashMap<String, Object> callOption) {
        this.callOption = callOption;
        return this;
    }

    public HashMap<String, Object> getArgumentsRequestType() {
        return argumentsRequestType;
    }

    public static HandleRequestType setTree(GenericItem tree) {
       return new HandleRequestType(tree);
    }

    private LinkedList<LinkedHashMap<String, Object>> buildArgList(AbstractAnnotationScanner scanner){
        LinkedList<LinkedHashMap<String, Object>> arglist = new LinkedList();
        try {
            if(scanner != null && scanner.getMapList().size() > 0){

                for(LinkedHashMap<String, Object> map : scanner.getMapList()){
                    LinkedHashMap<String, Object> hm = new LinkedHashMap();
                    Integer index = Integer.valueOf(scanner.get("index").asString());
                    String type = tree.getParametersTypes().get(index);
                    hm.put("index",map.get("index"));
                    hm.put("name",map.get("value"));
                    hm.put("type",type);
                    arglist.add(hm);
                }

            }
        } catch (NumberFormatException e) {
           logger.error("fail build Arg List ",e);
        }
        return arglist;
    }


    public HandleRequestType build(){
        try {
            handleRequestBody();
            handleRequestParam();
            handlePathVariable();
            handleRequestMapping();
            handlePostMapping();
            handleGetMapping();
            handleDeleteMapping();
            handlePutMapping();

        } catch (Exception e) {
            logger.error("fail Handle Request ",e);
        }
        return this;
    }



    private void handleRequestBody(){
        AbstractAnnotationScanner scanner = new AnnotationScanner().setPath("Lorg/springframework/web/bind/annotation/RequestBody;").setAnnotations(tree.getAnnotations()).scanner();
        if(scanner != null ){
            LinkedList<LinkedHashMap<String, Object>> args = buildArgList(scanner);
            argumentsRequestType.put("RequestBody",args);
        }
    }

    private void handleRequestMapping(){
        AbstractAnnotationScanner scanner = new AnnotationScanner().setPath("Lorg/springframework/web/bind/annotation/RequestMapping;").setAnnotations(tree.getAnnotations()).scanner();
        if(scanner != null ){
            if(scanner != null && scanner.getMapList().size() > 0) {
                for (Map<String, Object> map : scanner.getMapList()) {
                    if(map.containsKey("value")){
                        //callOption.put("path",map.get("value"));
                    }
                    if(map.containsKey("method")){
                       callOption.put("method", map.get("method"));
                    }
                    if(map.containsKey("params")){
                         callOption.put("params", map.get("params"));
                    }
                    if(map.containsKey("headers")){
                        callOption.put("headers",map.get("headers"));
                    }
                    if(map.containsKey("consumes")){
                        callOption.put("consumes",map.get("consumes"));
                    }
                    if(map.containsKey("produces")){
                          callOption.put("produces", map.get("produces"));
                    }
                }
            }

        }
    }

    private void handlePostMapping(){
        AbstractAnnotationScanner scanner = new AnnotationScanner().setPath("Lorg/springframework/web/bind/annotation/PostMapping;").setAnnotations(tree.getAnnotations()).scanner();
        if(scanner != null ){
            if(scanner != null && scanner.getMapList().size() > 0) {
                for (Map<String, Object> map : scanner.getMapList()) {
                    if(map.containsKey("value")){
                        //callOption.put("path",map.get("value"));
                    }
                    if(map.containsKey("name")){
                        callOption.put("name",map.get("name"));
                    }
                    if(map.containsKey("path")){
                        callOption.put("path",map.get("path"));
                    }
                    if(map.containsKey("params")){
                        callOption.put("params",map.get("params"));
                    }
                    if(map.containsKey("headers")){
                        callOption.put("headers",map.get("headers"));
                    }
                    if(map.containsKey("consumes")){
                        callOption.put("consumes",map.get("consumes"));
                    }
                    if(map.containsKey("produces")){
                        callOption.put("produces",map.get("produces"));
                    }
                    if(map.containsKey("method")){
                        callOption.put("method","POST");
                    }
                }
            }

        }
    }

    private void handleGetMapping(){
        AbstractAnnotationScanner scanner = new AnnotationScanner().setPath("Lorg/springframework/web/bind/annotation/GetMapping;").setAnnotations(tree.getAnnotations()).scanner();
        if(scanner != null ){
            if(scanner != null && scanner.getMapList().size() > 0) {
                for (Map<String, Object> map : scanner.getMapList()) {
                    if(map.containsKey("value")){
                       // callOption.put("path",map.get("value"));
                    }
                    if(map.containsKey("name")){
                        callOption.put("name",map.get("name"));
                    }
                    if(map.containsKey("path")){
                        callOption.put("path",map.get("path"));
                    }
                    if(map.containsKey("params")){
                        callOption.put("params",map.get("params"));
                    }
                    if(map.containsKey("headers")){
                        callOption.put("headers",map.get("headers"));
                    }
                    if(map.containsKey("consumes")){
                        callOption.put("consumes",map.get("consumes"));
                    }
                    if(map.containsKey("produces")){
                        callOption.put("produces",map.get("produces"));
                    }
                    if(map.containsKey("method")){
                        callOption.put("method","GET");
                    }
                }
            }

        }
    }

    private void handleDeleteMapping(){
        AbstractAnnotationScanner scanner = new AnnotationScanner().setPath("Lorg/springframework/web/bind/annotation/DeleteMapping;").setAnnotations(tree.getAnnotations()).scanner();
        if(scanner != null ){
            if(scanner != null && scanner.getMapList().size() > 0) {
                for (Map<String, Object> map : scanner.getMapList()) {
                    if(map.containsKey("value")){
                      //  callOption.put("path",map.get("value"));
                    }
                    if(map.containsKey("name")){
                        callOption.put("name",map.get("name"));
                    }
                    if(map.containsKey("path")){
                        callOption.put("path",map.get("path"));
                    }
                    if(map.containsKey("params")){
                        callOption.put("params",map.get("params"));
                    }
                    if(map.containsKey("headers")){
                        callOption.put("headers",map.get("headers"));
                    }
                    if(map.containsKey("consumes")){
                        callOption.put("consumes",map.get("consumes"));
                    }
                    if(map.containsKey("produces")){
                        callOption.put("produces",map.get("produces"));
                    }
                    if(map.containsKey("method")){
                        callOption.put("method","DELETE");
                    }
                }
            }

        }
    }

    private void handlePutMapping(){
        AbstractAnnotationScanner scanner = new AnnotationScanner().setPath("Lorg/springframework/web/bind/annotation/PutMapping;").setAnnotations(tree.getAnnotations()).scanner();
        if(scanner != null ){
            if(scanner != null && scanner.getMapList().size() > 0) {
                for (Map<String, Object> map : scanner.getMapList()) {
                    if(map.containsKey("value")){
                       // callOption.put("path",map.get("value"));
                    }
                    if(map.containsKey("name")){
                        callOption.put("name",map.get("name"));
                    }
                    if(map.containsKey("path")){
                        callOption.put("path",map.get("path"));
                    }
                    if(map.containsKey("params")){
                        callOption.put("params",map.get("params"));
                    }
                    if(map.containsKey("headers")){
                        callOption.put("headers",map.get("headers"));
                    }
                    if(map.containsKey("consumes")){
                        callOption.put("consumes",map.get("consumes"));
                    }
                    if(map.containsKey("produces")){
                        callOption.put("produces",map.get("produces"));
                    }
                    if(map.containsKey("method")){
                        callOption.put("method","PUT");
                    }
                }
            }

        }
    }

    private void handleRequestParam(){
        AbstractAnnotationScanner scanner = new AnnotationScanner().setPath("Lorg/springframework/web/bind/annotation/RequestParam;").setAnnotations(tree.getAnnotations()).scanner();
        if(scanner != null ){
            LinkedList<LinkedHashMap<String, Object>> args = buildArgList(scanner);
            argumentsRequestType.put("RequestParam",args);
        }
    }

    private void handlePathVariable(){
        AbstractAnnotationScanner scanner = new AnnotationScanner().setPath("Lorg/springframework/web/bind/annotation/PathVariable;").setAnnotations(tree.getAnnotations()).scanner();
        if(scanner != null ){
            LinkedList<LinkedHashMap<String, Object>> args = buildArgList(scanner);
            argumentsRequestType.put("PathVariable",args);
        }
    }
}
