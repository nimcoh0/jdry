package org.softauto.jaxrs.analyzer.analyzers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.analyzer.core.system.config.Configuration;
import org.softauto.analyzer.model.genericItem.GenericItem;

import java.util.*;

public class AnalyzeAnnotations {

    private static Logger logger = LogManager.getLogger(AnalyzeAnnotations.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

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

    private Set<String> jaxrsEndPoints = Collections
            .unmodifiableSet(new HashSet<>(Arrays.asList(".ws.rs.POST",".ws.rs.GET",".ws.rs.DELETE",".ws.rs.PUT")));

    public AnalyzeAnnotations setTree(GenericItem tree) {
        this.tree = tree;
        return this;
    }

    private boolean isEndPoint(){
          for(String s : jaxrsEndPoints ){
              if(tree.getAnnotations().toString().contains(s)){
                  return true;
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
                }
            }
                boolean start = false;
                for (Map.Entry entry : classList.entrySet()) {
                    if(isEndPoint() && entry.getKey().equals(tree.getNamespce())){
                        start = true;
                    }
                    if (start && ((Map) entry.getValue()).size() > 0) {
                        LinkedHashMap<String, Object> l = (LinkedHashMap<String, Object>) entry.getValue();
                        for (Map.Entry k : l.entrySet()) {
                            if (k.getKey().toString().contains(".ws.rs.Produces")) {
                                produce = ((Map) k.getValue()).get("value").toString() ;

                            }
                        }
                        if (tree.getAnnotations().containsKey("jakarta.ws.rs.Produces")) {
                            produce = ((Map) tree.getAnnotations().get("jakarta.ws.rs.Produces")).get("value").toString() ;

                        }else if (tree.getAnnotations().containsKey("javax.ws.rs.Produces")) {
                            produce = ((Map) tree.getAnnotations().get("javax.ws.rs.Produces")).get("value").toString() ;

                        }
                    }
                }
           // }
            logger.debug(JDRY,"successfully find produce  "+ produce);
        } catch (Exception e) {
            logger.error(JDRY,"fail find Produce ",e);
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
                }
            }
                boolean start = false;
                for (Map.Entry entry : classList.entrySet()) {
                    if(isEndPoint() && entry.getKey().equals(tree.getNamespce())){
                        start = true;
                    }
                    if (start && ((Map) entry.getValue()).size() > 0) {
                        LinkedHashMap<String, Object> l = (LinkedHashMap<String, Object>) entry.getValue();
                        for (Map.Entry k : l.entrySet()) {
                            if (k.getKey().toString().contains(".ws.rs.Consumes")) {
                                consume = ((Map) k.getValue()).get("value").toString() ;

                            }
                        }
                        if (tree.getAnnotations().containsKey("jakarta.ws.rs.Consumes")) {
                            consume = ((Map) tree.getAnnotations().get("jakarta.ws.rs.Consumes")).get("value").toString() ;

                        }else if (tree.getAnnotations().containsKey("javax.ws.rs.Consumes")) {
                            consume = ((Map) tree.getAnnotations().get("javax.ws.rs.Consumes")).get("value").toString() ;

                        }
                    }
                }
            logger.debug(JDRY,"successfully find consume  "+ consume);
        } catch (Exception e) {
            logger.error(JDRY,"fail find Consume ",e);
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
                }
            }
            String baseUrl = Configuration.has("base_uri") && Configuration.get("base_uri") != null ? Configuration.get("base_uri").asString() : "";
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
                                if (k.getKey().toString().contains("jakarta.ws.rs.Path")) {
                                    path = ((Map) k.getValue()).get("value") + "/" + path;

                                }else if (k.getKey().toString().contains("javax.ws.rs.Path")) {
                                    path = ((Map) k.getValue()).get("value") + "/" + path;

                                }

                            }
                            if (tree.getAnnotations().containsKey("jakarta.ws.rs.Path")) {
                                path = path + ((Map) tree.getAnnotations().get("jakarta.ws.rs.Path")).get("value");
                            }else if (tree.getAnnotations().containsKey("javax.ws.rs.Path")) {
                                path = path + ((Map) tree.getAnnotations().get("javax.ws.rs.Path")).get("value");
                            }
                            if(baseUrl != null && !baseUrl.isEmpty()) {
                                paths.add(baseUrl + "/" + path);
                            }else {
                                paths.add(path);
                            }
                        }
                    }
                }
            logger.debug(JDRY,"successfully aggregatePath path "+ Arrays.toString(paths.toArray()));
        } catch (Exception e) {
            logger.error(JDRY,"fail aggregatePath ",e);
        }
        return paths;
    }


}
