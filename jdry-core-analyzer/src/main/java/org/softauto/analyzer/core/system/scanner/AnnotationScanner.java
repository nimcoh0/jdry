package org.softauto.analyzer.core.system.scanner;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


public class AnnotationScanner extends AbstractAnnotationScanner{

    private static Logger logger = LogManager.getLogger(AnnotationScanner.class);

    boolean resolve = true;


    public AnnotationScanner setResolve(boolean resolve) {
        this.resolve = resolve;
        return this;
    }



    public  AnnotationScanner scanner(){
            if (isExist(path, annotations, parameter)) {
                LinkedList<LinkedHashMap<String, Object>> localmapList = visit(annotations, path);
                LinkedList<LinkedHashMap<String, Object>> newMapList = new LinkedList();
                for(LinkedHashMap<String, Object> map : localmapList){
                    LinkedHashMap<String, Object> newmap = SerializationUtils.clone(map);
                    newMapList.add(newmap);
                }
                resolve(newMapList);
                this.mapList = newMapList;
                return this;
            }
        return null;
    }

    public  Object resolve(Object o){
        try {
            //logger.debug("resolving "+o.toString());
            if(o instanceof ArrayList) {
                for (int i = 0; i < ((ArrayList) o).size(); i++) {
                    Object _o = resolve(((ArrayList) o).get(i));
                    ((ArrayList) o).set(i, _o);
                }
            }else if(o instanceof LinkedList<?>){
                    for(int i=0;i<((LinkedList)o).size();i++){
                        Object _o = resolve(((LinkedList)o).get(i));
                        ((LinkedList)o).set(i,_o);
                    }
            }else if(o instanceof Map){
                for(Map.Entry entry : ((Map<String,Object>)o).entrySet()){
                    Object _o = resolve(entry.getValue());
                    ((Map<String,Object>)o).put(entry.getKey().toString(),_o);
                }
            }else {
                o =  espl.evaluate(o.toString());
            }
        } catch (Exception e) {
            logger.error("fail resolve "+o.toString(),e);
        }

        return o;
    }


    private LinkedList<LinkedHashMap<String, Object>> visit(LinkedHashMap<String, Object> annotations,String path) {
        try {
            LinkedList<LinkedHashMap<String, Object>> mapList = null;
            if(has(annotations,"constructor")){
                mapList =  get(annotations,"constructor",path).getMapList();
            }
            if(mapList == null || mapList.size() == 0){
                mapList =  get(annotations,path).getMapList();
            }
            if(mapList != null) {
                //logger.debug("annotation " +path+ "retrieve successfully ");
               // mapList.stream().peek((item) -> logger.debug("key "+ item.keySet().toArray()[0] +" value "+ item.values().toArray()[0])).collect(Collectors.toList());
                return mapList;
            }
        } catch (Exception e) {
          logger.error("fail retrieve annotation for "+path,e);
        }
        return null;
    }

}
