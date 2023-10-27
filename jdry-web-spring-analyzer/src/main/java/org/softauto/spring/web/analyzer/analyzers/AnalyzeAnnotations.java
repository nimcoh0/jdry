package org.softauto.spring.web.analyzer.analyzers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.core.system.config.Configuration;

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
                            if (k.getKey().toString().contains("Lorg/springframework/web/bind/annotation/RequestMapping;")) {
                                if(((Map) k.getValue()).containsKey("produces")) {
                                    produce = ((Map) k.getValue()).get("produces").toString();
                                }

                            }
                        }
                        if (tree.getAnnotations().containsKey("Lorg/springframework/web/bind/annotation/RequestMapping;")) {
                            if(((Map) tree.getAnnotations().get("Lorg/springframework/web/bind/annotation/RequestMapping;")).containsKey("produces")) {
                                produce = ((Map) tree.getAnnotations().get("Lorg/springframework/web/bind/annotation/RequestMapping;")).get("produces").toString();
                            }

                        }
                    }
                }
            //}
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
                            if (k.getKey().toString().contains("Lorg/springframework/web/bind/annotation/RequestMapping;")) {
                                if(((Map) k.getValue()).containsKey("consumes")) {
                                    consume = ((Map) k.getValue()).get("consumes").toString();
                                }
                            }
                        }
                        if (tree.getAnnotations().containsKey("Lorg/springframework/web/bind/annotation/RequestMapping;")) {
                            if(((Map) tree.getAnnotations().get("Lorg/springframework/web/bind/annotation/RequestMapping;")).containsKey("consumes")) {
                                consume = ((Map) tree.getAnnotations().get("Lorg/springframework/web/bind/annotation/RequestMapping;")).get("consumes").toString();
                            }

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
        String baseUrl = Configuration.get("base_uri") != null ? Configuration.get("base_uri").asString() : "";
        try {
            if(classList == null || classList.size() ==0){
                classList = new LinkedHashMap();
                LinkedHashMap<String, Object> hm = new LinkedHashMap<>();
                if(tree.getAnnotations().containsKey("class")) {
                    classList.put(tree.getNamespce(), tree.getAnnotations().get("class"));
                    //classList.add(hm);
                }
            }
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
                                if (k.getKey().toString().contains("Lorg/springframework/web/bind/annotation/RequestMapping;")) {
                                    path = ((Map) k.getValue()).get("value") + "/" + path;
                                }
                            }
                            //if (tree.getAnnotations().containsKey("Lorg/springframework/web/bind/annotation/RequestMapping;")) {
                             //   path = path + ((Map) tree.getAnnotations().get("Lorg/springframework/web/bind/annotation/RequestMapping;")).get("value");
                            //}
                          if(baseUrl != null && !baseUrl.isEmpty()) {
                              paths.add(baseUrl + "/" + path);
                          }else {
                              paths.add(path);
                          }
                        }
                    }
                  String _path = null;
                  if (tree.getAnnotations().containsKey("Lorg/springframework/web/bind/annotation/RequestMapping;")) {
                      if(((Map) tree.getAnnotations().get("Lorg/springframework/web/bind/annotation/RequestMapping;")).containsKey("value"))
                      _path =  ((Map) tree.getAnnotations().get("Lorg/springframework/web/bind/annotation/RequestMapping;")).get("value").toString();
                  }
                  if(tree.getAnnotations().containsKey("Lorg/springframework/web/bind/annotation/PostMapping;")){
                      if(((Map)tree.getAnnotations().get("Lorg/springframework/web/bind/annotation/PostMapping;")).containsKey("value"))
                      _path =  ((Map)tree.getAnnotations().get("Lorg/springframework/web/bind/annotation/PostMapping;")).get("value").toString();
                  }
                  if(tree.getAnnotations().containsKey("Lorg/springframework/web/bind/annotation/GetMapping;")){
                      if(((Map)tree.getAnnotations().get("Lorg/springframework/web/bind/annotation/GetMapping;")).containsKey("value"))
                      _path =  ((Map)tree.getAnnotations().get("Lorg/springframework/web/bind/annotation/GetMapping;")).get("value").toString();
                  }
                  if(tree.getAnnotations().containsKey("Lorg/springframework/web/bind/annotation/DeleteMapping;")){
                      if(((Map)tree.getAnnotations().get("Lorg/springframework/web/bind/annotation/DeleteMapping;")).containsKey("value"))
                      _path =  ((Map)tree.getAnnotations().get("Lorg/springframework/web/bind/annotation/DeleteMapping;")).get("value").toString();
                  }
                  if(tree.getAnnotations().containsKey("Lorg/springframework/web/bind/annotation/PutMapping;")){
                      if(((Map)tree.getAnnotations().get("Lorg/springframework/web/bind/annotation/PutMapping;")).containsKey("value"))
                      _path =  ((Map)tree.getAnnotations().get("Lorg/springframework/web/bind/annotation/PutMapping;")).get("value").toString();
                  }
                  if(_path != null){
                      if(_path.startsWith("/")){
                          _path = _path.substring(1,_path.length());
                      }
                    paths.add(_path);
                  }
                }
            //}
            logger.debug("successfully aggregatePath path "+ Arrays.toString(paths.toArray()));
        } catch (Exception e) {
            logger.error("fail aggregatePath ",e);
        }
        return paths;
    }

}
