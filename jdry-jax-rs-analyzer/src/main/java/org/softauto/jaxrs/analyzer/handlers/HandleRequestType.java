package org.softauto.jaxrs.analyzer.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.analyzer.core.system.scanner.AbstractAnnotationScanner;
import org.softauto.analyzer.core.system.scanner.AnnotationScanner;
import org.softauto.analyzer.model.genericItem.GenericItem;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class HandleRequestType {

    protected static Logger logger = LogManager.getLogger(HandleRequestType.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    protected LinkedHashMap<String,Object> argumentsRequestType = new LinkedHashMap();

    protected GenericItem tree;



    public HandleRequestType(GenericItem tree){
        this.tree = tree;
    }

    public LinkedHashMap<String, Object> getArgumentsRequestType() {
        return argumentsRequestType;
    }

    public static HandleRequestType setTree(GenericItem tree) {
       return new HandleRequestType(tree);
    }

    public HandleRequestType build(){
        try {
            handleQueryParam();
            handlePathParam();
            handleBeanParam();
            handleHeaderParam();
            handleMatrixParam();
            handleFormParam();
            handleDefault();
        } catch (Exception e) {
            logger.error(JDRY,"fail Handle Request ",e);
        }
        return this;
    }

    protected LinkedList<LinkedHashMap<String, Object>> buildArgList(AbstractAnnotationScanner scanner){
        LinkedList<LinkedHashMap<String, Object>> arglist = new LinkedList();
        try {
            if(scanner != null && scanner.getMapList().size() > 0){
                LinkedHashMap<String, Object> hm = new LinkedHashMap();
                for(Map<String, Object> map : scanner.getMapList()){
                     Integer index = Integer.valueOf(scanner.get("index").asString());
                     String type = tree.getParametersTypes().get(index);
                     hm.put("index",map.get("index"));
                     hm.put("name",map.get("value"));
                     hm.put("type",type);
                }
                arglist.add(hm);
            }
        } catch (NumberFormatException e) {
            logger.error(JDRY,"fail build Arg List ",e);
        }
        return arglist;
    }

    protected LinkedList<LinkedHashMap<String, Object>> buildArgList(){
        LinkedList<LinkedHashMap<String, Object>> arglist = new LinkedList();
        try {
            if(tree.getParametersTypes().size() > 0){
                LinkedHashMap<String, Object> hm = new LinkedHashMap();
                for(int i=0;i<tree.getParametersTypes().size();i++){
                    hm.put("index",i);
                    hm.put("name",tree.getArgumentsNames().get(i));
                    hm.put("type",tree.getParametersTypes().get(i));
                }
                arglist.add(hm);
            }
        } catch (NumberFormatException e) {
            logger.error(JDRY,"fail build Arg List ",e);
        }
        return arglist;
    }


    protected void handleQueryParam(){
        AbstractAnnotationScanner scanner = null;
        if(tree.getAnnotations().containsKey("Ljakarta/ws/rs/QueryParam;")) {
            scanner = new AnnotationScanner().setPath("Ljakarta/ws/rs/QueryParam;").setAnnotations(tree.getAnnotations()).scanner();
        }
        if(tree.getAnnotations().containsKey("Ljavax/ws/rs/QueryParam;")) {
            scanner = new AnnotationScanner().setPath("Ljavax/ws/rs/QueryParam;").setAnnotations(tree.getAnnotations()).scanner();
        }
        if(scanner != null ){
            LinkedList<LinkedHashMap<String, Object>> args = buildArgList(scanner);
            argumentsRequestType.put("QueryParam",args);
        }
    }

    protected void handleFormParam(){
        AbstractAnnotationScanner scanner = null;
        if(tree.getAnnotations().containsKey("Ljakarta/ws/rs/FormParam;")) {
            scanner = new AnnotationScanner().setPath("Ljakarta/ws/rs/FormParam;").setAnnotations(tree.getAnnotations()).scanner();
        }
        if(tree.getAnnotations().containsKey("Ljavax/ws/rs/FormParam;")) {
            scanner = new AnnotationScanner().setPath("Ljavax/ws/rs/FormParam;").setAnnotations(tree.getAnnotations()).scanner();
        }
        if(scanner != null ){
            LinkedList<LinkedHashMap<String, Object>> args = buildArgList(scanner);
            argumentsRequestType.put("FormParam",args);
        }
    }

    protected void handleDefault(){
        if(argumentsRequestType.size() == 0) {
            LinkedList<LinkedHashMap<String, Object>> args = buildArgList();
            if (args != null && args.size() > 0)
                argumentsRequestType.put("FormParam", args);
        }
    }

    protected void handlePathParam(){
        AbstractAnnotationScanner scanner = null;
        if(tree.getAnnotations().containsKey("Ljakarta/ws/rs/PathParam;")) {
            scanner = new AnnotationScanner().setPath("Ljakarta/ws/rs/PathParam;").setAnnotations(tree.getAnnotations()).scanner();
        }
        if(tree.getAnnotations().containsKey("Ljavax/ws/rs/PathParam;")) {
            scanner = new AnnotationScanner().setPath("Ljavax/ws/rs/PathParam;").setAnnotations(tree.getAnnotations()).scanner();
        }
        if(scanner != null ){
            LinkedList<LinkedHashMap<String, Object>> args = buildArgList(scanner);
            argumentsRequestType.put("PathParam",args);
        }
    }

    protected void handleBeanParam(){
        AbstractAnnotationScanner scanner = null;
        if(tree.getAnnotations().containsKey("Ljakarta/ws/rs/BeanParam;")) {
            scanner = new AnnotationScanner().setPath("Ljakarta/ws/rs/BeanParam;").setAnnotations(tree.getAnnotations()).scanner();
        }
        if(tree.getAnnotations().containsKey("Ljavax/ws/rs/BeanParam;")) {
            scanner = new AnnotationScanner().setPath("Ljavax/ws/rs/BeanParam;").setAnnotations(tree.getAnnotations()).scanner();
        }
        if(scanner != null ){
            LinkedList<LinkedHashMap<String, Object>> args = buildArgList(scanner);
            argumentsRequestType.put("BeanParam",args);
        }
    }

    protected void handleHeaderParam(){
        AbstractAnnotationScanner scanner = null;
        if(tree.getAnnotations().containsKey("Ljakarta/ws/rs/HeaderParam;")) {
            scanner = new AnnotationScanner().setPath("Ljakarta/ws/rs/HeaderParam;").setAnnotations(tree.getAnnotations()).scanner();
        }
        if(tree.getAnnotations().containsKey("Ljavax/ws/rs/HeaderParam;")) {
            scanner = new AnnotationScanner().setPath("Ljavax/ws/rs/HeaderParam;").setAnnotations(tree.getAnnotations()).scanner();
        }
        if(scanner != null ){
            LinkedList<LinkedHashMap<String, Object>> args = buildArgList(scanner);
            argumentsRequestType.put("HeaderParam",args);
        }
    }

    protected void handleMatrixParam(){
        AbstractAnnotationScanner scanner = null;
        if(tree.getAnnotations().containsKey("Ljakarta/ws/rs/MatrixParam;")) {
            scanner = new AnnotationScanner().setPath("Ljakarta/ws/rs/MatrixParam;").setAnnotations(tree.getAnnotations()).scanner();
        }
        if(tree.getAnnotations().containsKey("Ljavax/ws/rs/MatrixParam;")) {
            scanner = new AnnotationScanner().setPath("Ljavax/ws/rs/MatrixParam;").setAnnotations(tree.getAnnotations()).scanner();
        }
        if(scanner != null ){
            LinkedList<LinkedHashMap<String, Object>> args = buildArgList(scanner);
            argumentsRequestType.put("MatrixParam",args);
        }
    }
}
