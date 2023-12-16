package org.softauto.analyzer.core.system.scanner;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.LinkedHashMap;
import java.util.LinkedList;


public class AnnotationScanner extends AbstractAnnotationScanner{

    private static Logger logger = LogManager.getLogger(AnnotationScanner.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");


    public  AnnotationScanner scanner(){
            if (isExist(path, annotations, parameter)) {
                LinkedList<LinkedHashMap<String, Object>> localmapList = visit(annotations, path);
                LinkedList<LinkedHashMap<String, Object>> newMapList = new LinkedList();
                for(LinkedHashMap<String, Object> map : localmapList){
                    LinkedHashMap<String, Object> newmap = SerializationUtils.clone(map);
                    newMapList.add(newmap);
                }

                this.mapList = newMapList;
                return this;
            }
        return null;
    }




    private LinkedList<LinkedHashMap<String, Object>> visit(LinkedHashMap<String, Object> annotations,String path) {
        try {
            LinkedList<LinkedHashMap<String, Object>> mapList = null;
            if(has(annotations,"constructor")){
                mapList =  get(annotations,"constructor",path).getMapList();
            }
            if(has(annotations,"class")){
                mapList =  get(annotations,"class",path).getMapList();
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
          logger.error(JDRY,"fail retrieve annotation for "+path,e);
        }
        return null;
    }

}
