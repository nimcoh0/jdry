package org.softauto.analyzer.core.system.scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.core.system.espl.Espl;

import java.util.*;

public abstract class AbstractAnnotationScanner extends AnnotationHelper{

    private static Logger logger = LogManager.getLogger(AbstractAnnotationScanner.class);

    public abstract AnnotationScanner scanner();

    public abstract  Object resolve(Object o);

    /**
     * list of obj for publish/resolve
     */
    protected List<Object> obj = new ArrayList<>();

    /**
     * annotation list
     */
    protected LinkedHashMap<String, Object> annotations;

    Espl espl = Espl.getInstance();

    String path;

    String parameter;

    Object element;

    LinkedList<LinkedHashMap<String, Object>> mapList = new LinkedList();

    LinkedHashMap<String,Object> parameters = new LinkedHashMap();

    public LinkedList<LinkedHashMap<String, Object>> getMapList() {
        return mapList;
    }

    public LinkedHashMap<String, Object> getParameters() {
        return parameters;
    }

    public AbstractAnnotationScanner setParameters(LinkedHashMap<String, Object> parameters) {
        this.parameters = parameters;
        return this;
    }

    public AbstractAnnotationScanner addParameter(String key,Object value) {
        this.parameters.put(key,value);
        return this;
    }

    public AbstractAnnotationScanner setMapList(LinkedList<LinkedHashMap<String, Object>> mapList) {
        this.mapList = mapList;
        return this;
    }

    public  Object asObject(){
        if(element!= null ) {
            return element;
        }
        return null;
    }

    public  Map<?,?> asMap(){
        if(element!= null ) {
            return (Map<?,?>)element;
        }
        return null;
    }

    public  String asString(){
        if(element!= null ) {
            return element.toString();
        }
        return null;
    }

    public <R> LinkedList<R> asLinkedList(){
        if(element != null ) {
            if(element instanceof ArrayList) {
                return (LinkedList<R>) element;
            }else {
                LinkedList<R> list = new LinkedList<>();
                list.add((R)element);
                return list;
            }

        }
        return null;
    }

    public <R> List<R> asList(){
        if(element != null ) {
            if(element instanceof ArrayList) {
                return (List<R>) element;
            }else {
                List<R> list = new ArrayList<>();
                list.add((R)element);
                return list;
            }

        }
        return null;
    }



    public AbstractAnnotationScanner get(String key){
        for(Map<?,?> map : mapList) {
            if (map.containsKey(key)) {
                element = map.get(key);
                return this;
            } else {
                element = null;
            }
        }
       return this;
    }

    public boolean has(String key){
        if(key != null) {
            for (Map<?, ?> map : mapList) {
                if (map.containsKey(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    public AbstractAnnotationScanner setParameter(String parameter) {
        this.parameter = parameter;
        return this;
    }

    public AbstractAnnotationScanner setPath(String path) {
        this.path = path;
        return this;
    }

    public Map<String, Object> getMap(int index) {
        if(mapList!= null && mapList.size()> 0)
            return mapList.get(index);
        return null;
    }



    public AbstractAnnotationScanner setAnnotations(LinkedHashMap<String, Object> annotations) {
        this.annotations = annotations;
        return this;
    }

    public AbstractAnnotationScanner addObj(Object obj) {
        this.obj.add(obj);
        return this;
    }

    public AbstractAnnotationScanner addObj(Object...obj) {
        for(Object o : obj){
            addObj(o);
        }
        return this;
    }

    public AbstractAnnotationScanner addObj(String key,Object value) {
        LinkedHashMap<String,Object>  hm = new LinkedHashMap();
        hm.put(key,value);
        this.obj.add(hm);
        return this;
    }




}
