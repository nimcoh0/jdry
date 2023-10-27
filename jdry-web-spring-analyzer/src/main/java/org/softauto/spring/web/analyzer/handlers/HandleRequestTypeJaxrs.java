package org.softauto.spring.web.analyzer.handlers;

import org.softauto.analyzer.core.system.config.Configuration;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.core.system.scanner.AbstractAnnotationScanner;

import java.util.*;

public class HandleRequestTypeJaxrs {

    protected HashMap<String,Object> callOption = new HashMap<>();

    protected  GenericItem tree;

    public  HandleRequestTypeJaxrs setTree(GenericItem tree) {
        this.tree = tree;
        return this;
    }

    AbstractAnnotationScanner scanner;

    public HandleRequestTypeJaxrs setScanner(AbstractAnnotationScanner scanner) {
        this.scanner = scanner;
        return this;
    }

    public  HandleRequestTypeJaxrs setCallOption(HashMap<String, Object> callOption) {
        this.callOption = callOption;
        return this;
    }

    public  HandleRequestTypeJaxrs build(){
        try {

            handleJaxrs();
        } catch (Exception e) {
            e.printStackTrace();
          //  logger.error("fail Handle Request ",e);
        }
        return this;
    }

    protected  LinkedList<LinkedHashMap<String, Object>> buildArgList(){
        LinkedList<LinkedHashMap<String, Object>> arglist = new LinkedList();
        try {
            if(tree != null && tree.getParametersTypes().size() > 0){

                for(int i=0;i<tree.getParametersTypes().size();i++){
                    LinkedHashMap<String, Object> hm = new LinkedHashMap();
                    hm.put("index",i);
                    hm.put("name",tree.getArgumentsNames().get(i));
                    hm.put("type",tree.getParametersTypes().get(i));
                    arglist.add(hm);
                }

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            //logger.error("fail build Arg List ",e);
        }
        return arglist;
    }

    protected  void handleJaxrs(){
        HashMap<String,Object> hm = new HashMap<>();
        //AbstractAnnotationScanner scanner = new AnnotationScanner().setPath("Lorg/softauto/jaxrs/annotations/JAXRS;").setAnnotations(tree.getAnnotations()).scanner();
        if(scanner != null ){
            String requestType = null;
            LinkedList<LinkedHashMap<String, Object>> args = buildArgList();
            for(LinkedHashMap<String, Object> map : scanner.getMapList()){
                for(Map.Entry entry : map.entrySet()){
                    if(entry.getKey().equals("mapping")){
                        requestType = entry.getValue().toString();
                    }
                }
            }
            if(requestType ==null && Configuration.has("default_jaxrs_unknown_request_type")){
                requestType = Configuration.get("default_jaxrs_unknown_request_type").asString();
            }
            if(requestType !=null) {
                hm.put(requestType, args);
                callOption.put("argumentsRequestType",hm);
            }else {
               // logger.error("no args request type found for "+ tree.getName());
            }
        }
    }
}
