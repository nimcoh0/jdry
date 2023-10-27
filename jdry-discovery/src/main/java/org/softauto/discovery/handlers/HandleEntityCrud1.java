package org.softauto.discovery.handlers;

import org.softauto.config.Configuration;
import org.softauto.espl.Espl;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class HandleEntityCrud1 {

    CallGraph cg;

    //boolean reader;

    //boolean writer;

    //String entity;

    //String method;



    //public HandleEntityCrud setMethod(String method) {
       // this.method = method;
      //  return this;
   // }

    private List<HashMap<String,String>> crudToEntity = new ArrayList<>();


    public List<HashMap<String,String>> getCrudToEntity() {
        return crudToEntity;
    }

    private boolean isCrudToEntityExist(String key,String value){
        for(HashMap<String,String> rec : crudToEntity){
            if(rec.containsKey(key) && rec.containsValue(value)){
                return true;
            }
        }
        return false;
    }



    public HandleEntityCrud1 setCg(CallGraph cg) {
        this.cg = cg;
        return this;
    }

    /*
    private String findEntity(List<Type> types){
       Espl.getInstance().addProperty("method",types);
       for(Type t : types){
           if(isEntity(t.toString())){
               return t.toString();
           }
       }
       return null;
    }

     */

    public static String findEntity(SootMethod sootMethod,String schema){
        try {
                Object o = Espl.getInstance().addProperty("method",sootMethod).evaluate(schema);
                if(o != null ) {
                    if (o instanceof ArrayList<?> ) {
                        if(((ArrayList) o).size() > 0) {
                            return ((ArrayList) o).get(0).toString();
                        }
                    } else {
                        return o.toString();
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    public static boolean isEntity(String name){
        try {
            if(Configuration.has("entity_identify")){
                String s = Configuration.get("entity_identify").asString();
                return (boolean) Espl.getInstance().addRootObject(name).evaluate(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

     */

    public HandleEntityCrud1 build(){
        Iterator i = cg.iterator();
        boolean result = false;

        while (i.hasNext()) {
            Edge sm = (Edge) i.next();
            Espl.getInstance().addProperty("body",sm.getSrc().method().getActiveBody().toString().toLowerCase());
            if(Configuration.has("entity_create_statement")) {
                String s = Configuration.get("entity_create_statement").asString();
                result = (Boolean) Espl.getInstance().addProperty("method",sm.getSrc().method()).evaluate(s);
                if(result){
                    String entity = null;
                    if(Configuration.has("entity_create_identify")) {
                        String schema = Configuration.get("entity_create_identify").asString();
                        entity = findEntity(sm.getSrc().method(),schema);
                    }


                        //writer = true;
                        if(entity != null ) {
                            if(!isCrudToEntityExist("create",entity)) {
                                HashMap<String,String> hm = new HashMap<>();
                                hm.put("create",entity);
                                crudToEntity.add(hm);
                            }
                        }else {
                            if(!isCrudToEntityExist("create",null)) {
                                HashMap<String, String> hm = new HashMap<>();
                                hm.put("create", null);
                                crudToEntity.add(hm);
                            }
                        }
                }
            }
            if(Configuration.has("entity_read_statement")) {
                String s = Configuration.get("entity_read_statement").asString();
                result = (Boolean) Espl.getInstance().addProperty("method",sm.getSrc().method()).evaluate(s);
                if(result){
                    String entity = null;
                    if(Configuration.has("entity_read_identify")) {
                        String schema = Configuration.get("entity_read_identify").asString();
                        entity = findEntity(sm.getSrc().method(),schema);
                    }
                        //entity = findEntity(sm.getSrc().method());
                        //entity = findEntity(Arrays.asList(new Type[]{sm.getSrc().method().getReturnType()}));
                        //reader = true;
                        if(entity != null) {
                            if(!isCrudToEntityExist("read",entity)) {
                                HashMap<String,String> hm = new HashMap<>();
                                hm.put("read",entity);
                                crudToEntity.add(hm);
                            }
                        }
                }
            }
            if(Configuration.has("entity_update_statement")) {
                String s = Configuration.get("entity_update_statement").asString();
                result = (Boolean) Espl.getInstance().addProperty("method",sm.getSrc().method()).evaluate(s);
                if(result){
                    String entity = null;
                    if(Configuration.has("entity_update_identify")) {
                        String schema = Configuration.get("entity_update_identify").asString();
                        entity = findEntity(sm.getSrc().method(),schema);
                    }
                    //entity = findEntity(sm.getSrc().method().getParameterTypes());
                    //reader = true;
                    if(entity != null) {
                        if(!isCrudToEntityExist("update",entity)) {
                            HashMap<String,String> hm = new HashMap<>();
                            hm.put("update",entity);
                            crudToEntity.add(hm);
                        }
                    }

                }
            }
            if(Configuration.has("entity_delete_statement")) {
                String s = Configuration.get("entity_delete_statement").asString();
                result = (Boolean) Espl.getInstance().addProperty("method",sm.getSrc().method()).evaluate(s);
                if(result){
                    String entity = null;
                    if(Configuration.has("entity_delete_identify")) {
                        String schema = Configuration.get("entity_delete_identify").asString();
                        entity = findEntity(sm.getSrc().method(),schema);
                    }

                    //sm.getSrc().method().getParameterTypes().get(0)..getDefaultFinalType().
                    //entity = findEntity(sm.getSrc().method().getParameterTypes());
                    //reader = true;
                    if(entity != null) {
                        if(!isCrudToEntityExist("delete",entity)) {
                            HashMap<String,String> hm = new HashMap<>();
                            hm.put("delete",entity);
                            crudToEntity.add(hm);
                        }
                    }
                }
            }
        }
        return this;
    }
}
