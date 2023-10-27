package org.softauto.jaxrs.analyzer.analyzers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.core.system.config.Configuration;
import org.softauto.analyzer.model.genericItem.GenericItem;

import java.util.*;

public class AnalyzeAnnotations {

    private static Logger logger = LogManager.getLogger(AnalyzeAnnotations.class);

    private GenericItem tree;

    private List<String> pathList = new ArrayList<>();

    private String produce;

    private String consume;


    public List<String> getPathList() {
        return pathList;
    }

    public String getProduce() {
        return produce;
    }

    public String getConsume() {
        return consume;
    }

    public AnalyzeAnnotations setTree(GenericItem tree) {
        this.tree = tree;
        return this;
    }

    private boolean isEndPoint(){
        if(Configuration.has("jax_rs_end_point")){
          List<String> endPointsList =   Configuration.get("jax_rs_end_point").asList();
          for(String s : endPointsList ){
              if(tree.getAnnotations().toString().contains(s)){
                  return true;
              }
          }
        }
       return false;
    }

    public AnalyzeAnnotations build(){
        LinkedHashMap<String, Object> classList =  tree.getClassList();
        pathList = aggregatePath(classList);
        produce = findProduce(classList);
        consume = findConsume(classList);
        return this;
    }




    private String findProduce(LinkedHashMap<String, Object> classList){
        String produce = "";
        try {
            if(classList == null || classList.size() ==0){
                classList = new LinkedHashMap();
                LinkedHashMap<String, Object> hm = new LinkedHashMap<>();
                if(tree.getAnnotations().containsKey("class")) {
                    classList.put(tree.getNamespce(), tree.getAnnotations().get("class"));
                    //classList.putAll(hm);
                }
            }
           // for(LinkedHashMap<String, Object> hm : classList){
                boolean start = false;
                for (Map.Entry entry : classList.entrySet()) {
                    if(isEndPoint() && entry.getKey().equals(tree.getNamespce())){
                        start = true;
                    }
                    if (start && ((Map) entry.getValue()).size() > 0) {
                        LinkedHashMap<String, Object> l = (LinkedHashMap<String, Object>) entry.getValue();
                        for (Map.Entry k : l.entrySet()) {
                            if (k.getKey().toString().contains("/ws/rs/Produces;")) {
                                produce = ((Map) k.getValue()).get("value").toString() ;

                            }
                        }
                        if (tree.getAnnotations().containsKey("Ljakarta/ws/rs/Produces;")) {
                            produce = ((Map) tree.getAnnotations().get("Ljakarta/ws/rs/Produces;")).get("value").toString() ;

                        }else if (tree.getAnnotations().containsKey("Ljavax/ws/rs/Produces;")) {
                            produce = ((Map) tree.getAnnotations().get("Ljavax/ws/rs/Produces;")).get("value").toString() ;

                        }
                    }
                }
           // }
            logger.debug("successfully find produce  "+ produce);
        } catch (Exception e) {
            logger.error("fail find Produce ",e);
        }
        return produce;
    }

    private String findConsume(LinkedHashMap<String, Object> classList){
        String consume = "";
        try {
            if(classList == null || classList.size() ==0){
                classList = new LinkedHashMap();
                LinkedHashMap<String, Object> hm = new LinkedHashMap<>();
                if(tree.getAnnotations().containsKey("class")) {
                    classList.put(tree.getNamespce(), tree.getAnnotations().get("class"));
                    //classList.add(hm);
                }
            }
            //for(LinkedHashMap<String, Object> hm : classList){
                boolean start = false;
                for (Map.Entry entry : classList.entrySet()) {
                    if(isEndPoint() && entry.getKey().equals(tree.getNamespce())){
                        start = true;
                    }
                    if (start && ((Map) entry.getValue()).size() > 0) {
                        LinkedHashMap<String, Object> l = (LinkedHashMap<String, Object>) entry.getValue();
                        for (Map.Entry k : l.entrySet()) {
                            if (k.getKey().toString().contains("/ws/rs/Consumes;")) {
                                consume = ((Map) k.getValue()).get("value").toString() ;

                            }
                        }
                        if (tree.getAnnotations().containsKey("Ljakarta/ws/rs/Consumes;")) {
                            consume = ((Map) tree.getAnnotations().get("Ljakarta/ws/rs/Consumes;")).get("value").toString() ;

                        }else if (tree.getAnnotations().containsKey("Ljavax/ws/rs/Consumes;")) {
                            consume = ((Map) tree.getAnnotations().get("Ljavax/ws/rs/Consumes;")).get("value").toString() ;

                        }
                    }
                }
            //}
            logger.debug("successfully find consume  "+ consume);
        } catch (Exception e) {
            logger.error("fail find Consume ",e);
        }
        return consume;
    }





    private List<String> aggregatePath(LinkedHashMap<String, Object> classList){
        List<String> paths = new ArrayList<>();
        try {
            if(classList == null || classList.size() ==0){
                classList = new LinkedHashMap();
                LinkedHashMap<String, Object> hm = new LinkedHashMap<>();
                if(tree.getAnnotations().containsKey("class")) {
                    classList.put(tree.getNamespce(), tree.getAnnotations().get("class"));
                    //classList.add(hm);
                }
            }
            String baseUrl = Configuration.has("base_uri") && Configuration.get("base_uri") != null ? Configuration.get("base_uri").asString() : "";
           // for(LinkedHashMap<String, Object> hm : classList){
              if (isEndPoint() && classList.keySet().contains(tree.getNamespce())) {
                    String path = "";
                    boolean start = false;
                    for (Map.Entry entry : classList.entrySet()) {
                        if(entry.getKey().equals(tree.getNamespce())){
                            start = true;
                        }
                        if (start && ((Map) entry.getValue()).size() > 0) {
                            LinkedHashMap<String, Object> l = (LinkedHashMap<String, Object>) entry.getValue();
                            for (Map.Entry k : l.entrySet()) {
                                if (k.getKey().toString().contains("Ljakarta/ws/rs/Path;")) {
                                    path = ((Map) k.getValue()).get("value") + "/" + path;

                                }else if (k.getKey().toString().contains("Ljavax/ws/rs/Path;")) {
                                    path = ((Map) k.getValue()).get("value") + "/" + path;

                                }

                            }
                            if (tree.getAnnotations().containsKey("Ljakarta/ws/rs/Path;")) {
                                path = path + ((Map) tree.getAnnotations().get("Ljakarta/ws/rs/Path;")).get("value");
                            }else if (tree.getAnnotations().containsKey("Ljavax/ws/rs/Path;")) {
                                path = path + ((Map) tree.getAnnotations().get("Ljavax/ws/rs/Path;")).get("value");
                            }
                            if(baseUrl != null && !baseUrl.isEmpty()) {
                                paths.add(baseUrl + "/" + path);
                            }else {
                                paths.add(path);
                            }
                        }
                    }
                }
           // }
            logger.debug("successfully aggregatePath path "+ Arrays.toString(paths.toArray()));
        } catch (Exception e) {
            logger.error("fail aggregatePath ",e);
        }
        return paths;
    }


}
