package org.softauto.discovery;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.Discover;
import org.softauto.clazz.ClassInfo;
import org.softauto.clazz.ClassInfoBuilder;
import org.softauto.config.Context;
import org.softauto.config.Configuration;
import org.softauto.core.ApplyRule;
import org.softauto.filter.Filter;
import org.softauto.filter.FilterByAnnotation;
import org.softauto.filter.FilterByDomain;
import org.softauto.handlers.HandleEntity;
import org.softauto.handlers.HandleFiledDiscovery;
import org.softauto.handlers.HandleFlowDiscovery;
import org.softauto.handlers.HandleMethodDiscovery;
import org.softauto.flow.FlowObject;
import org.softauto.model.entity.EntityFactory;
import org.softauto.spel.SpEL;
import org.softauto.handlers.annotations.HandelClassAnnotation;
import org.softauto.model.clazz.ClassFactory;
import org.softauto.model.field.FieldFactory;
import org.softauto.model.item.*;
import org.softauto.utils.Entity;
import org.softauto.utils.Util;
import soot.*;
import soot.jimple.JimpleBody;
import soot.jimple.Stmt;
import soot.jimple.spark.SparkTransformer;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.ReachableMethods;

import soot.util.queue.QueueReader;

import java.util.*;

public class Discovery extends AbstractDiscovery {

    private static Logger logger = LogManager.getLogger(Discover.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    private static HashMap<String,Integer> nameCounter = new HashMap<>();

    static {
        soot.options.Options.v().set_keep_line_number(true);
        soot.options.Options.v().set_whole_program(true);
        soot.options.Options.v().setPhaseOption("cg","verbose:true");
    }


    private List<String> evaluateList(List<String> beforeEvaluateList){
        List<String> afterEvaluateList = new ArrayList<>();
        for(String str : beforeEvaluateList){
            Object o = SpEL.getInstance().evaluate(str);
            if(o != null)
                afterEvaluateList.add(o.toString());
        }
        return afterEvaluateList;
    }

    List<String> unboxList = Configuration.has(Context.UNBOX_RETURN_TYPE) ? evaluateList(Configuration.get(Context.UNBOX_RETURN_TYPE).asList()): new ArrayList<>();
    List<String> unboxExcludeList = Configuration.has(Context.UNBOX_EXCLUDE_RETURN_TYPE) ? evaluateList(Configuration.get(Context.UNBOX_EXCLUDE_RETURN_TYPE).asList()): new ArrayList<>();
    List<String> unboxExcludeMethodList = Configuration.has("unbox_exclude_method") ? evaluateList(Configuration.get("unbox_exclude_method").asList()): new ArrayList<>();



    protected List<FlowObject> flowDiscovery(SootMethod mainMethod){
        List<FlowObject> flowObjects = new ArrayList<>();
        try {

            logger.debug(JDRY,"flow  discovery for " + mainMethod );
            SootClass c = Scene.v().loadClass(mainMethod.getDeclaringClass().toString(), SootClass.BODIES);
            c.setApplicationClass();
            SootMethod entryPoint = c.getMethod(mainMethod.getName(),mainMethod.getParameterTypes());
            List<SootMethod> entryPoints = new ArrayList();
            entryPoints.add(entryPoint);
            Scene.v().setEntryPoints(entryPoints);
            Scene.v().loadNecessaryClasses();
            CHATransformer.v().transform();
            CallGraph cg = Scene.v().getCallGraph();

            List<SootMethod> lm = Scene.v().getEntryPoints();
            for (SootMethod em: lm) {
                ReachableMethods rm;
                rm = new ReachableMethods(cg, Collections.singleton(em));
                rm.update();
                QueueReader<MethodOrMethodContext> qr = rm.listener();

                SootMethod m = null;
                SpEL.getInstance().addProperty("method",m);
                while (qr.hasNext()) {
                    MethodOrMethodContext momc = qr.next();
                    if (momc != null) {
                        m = momc.method();
                        if (m.isConcrete()) {
                            if (Filter.filter(new FilterByDomain().set(Configuration.get(Context.DOMAIN).asString()), m.getDeclaringClass()) && !m.getName().contains("$")) {
                                if(!unboxList.contains(m.getDeclaringClass().getName()) && !unboxExcludeList.contains(m.getDeclaringClass().getName()) && !Util.isEntity(m.getDeclaringClass()))
                                {
                                    flowObjects.add(HandleFlowDiscovery.handleFlowDiscovery(new MethodTreeDiscovery().setCallGraph(cg).setFilter(new FilterByDomain().set(Configuration.get(Context.DOMAIN).asString())), m));
                                }
                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
            logger.error(JDRY,"fail flow Discovery ",e.getMessage());
        }

        return flowObjects;
    }




    protected List<Object> discovery(){
        List<Object> sootItems = new ArrayList<>();
        try {
            CHATransformer.v().transform();
            CallGraph cg = Scene.v().getCallGraph();
            for(SootClass sc : Scene.v().getApplicationClasses()) {
                if (Filter.filter(new FilterByDomain().set(Configuration.get(Context.DOMAIN).asString()), sc)) {
                    SpEL.getInstance().addProperty("class",sc);
                    if(sc.getModifiers() > 0) {
                        if (!isInClazzList(sc.getName())) {
                            clazzes.add(sc.getName());
                            if(!Util.isEntity(sc)) {
                                for (SootField f : sc.getFields()) {
                                    // SootField sootField = HandleFiledDiscovery.filedDiscovery(new DiscoveryFieldByAnnotation().set(Configuration.get(Context.DISCOVER_FIELD_BY_ANNOTATION).asList()), f);
                                    //if (sootField != null) {
                                   // sootItems.add(f);
                                    // }
                                }
                            }
                        }
                    }
                    for (SootMethod m : sc.getMethods()) {
                        SpEL.getInstance().addProperty("method",m);
                        SootMethod sootMethod = HandleMethodDiscovery.handleMethodDiscovery(new DiscoveryByAnnotation().set(Configuration.get(Context.DISCOVER_BY_ANNOTATION).asList()), m);
                        if(sootMethod != null){
                            sootItems.add(sootMethod);
                        }
                    }
                    sootItems.add(sc);
                    HandleEntity handleEntity = new HandleEntity().setClazz(sc).build();
                    if(handleEntity.isEntity()){
                        sootItems.add(new Entity().setEntity(sc));
                    }

                }
            }
        } catch (Exception e) {
            logger.error(JDRY,"fail method Discovery ",e.getMessage());
        }
        return sootItems;
    }

    private static String buildName(Item item){

           String name = null;
        if(item != null) {
           if (nameCounter.containsKey(item.getNamespce() + "." + item.getName())) {
               Integer i = nameCounter.get(item.getNamespce() + "." + item.getName());
               name = item.getNamespce() + "." + item.getName() + "_" + i;
               nameCounter.put(item.getNamespce() + "." + item.getName(), i+1);
           } else {
               nameCounter.put(item.getNamespce() + "." + item.getName(), 0);
               name = item.getNamespce() + "." + item.getName();
           }
       }
        return name;
    }


    public Discovery discover(){

        PackManager.v().getPack("wjtp").add(new Transform("wjtp.myTrans", new SceneTransformer() {

            @Override
            protected void internalTransform(String phaseName, Map options) {

                List<Object> sootItems = discovery();
                logger.debug(JDRY,"successfully discover " + sootItems.size() + " sootItems");

                discovery.put("name",Configuration.get(Context.FILE_NAME).asString());
                discovery.put("namespace",Configuration.get(Context.FILE_NAMESPACE).asString());
                discovery.put("version",Configuration.get(Context.VERSION).asString());
                discovery.put("doc",Configuration.get(Context.DOC).asString());
                discovery.set("methods",new ObjectMapper().createObjectNode());
                discovery.set("classes",new ObjectMapper().createObjectNode());
                discovery.set("entities",new ObjectMapper().createObjectNode());
                discovery.set("fields",new ObjectMapper().createObjectNode());
                //int count = 0;
                for(Object o : sootItems){
                    try {

                         if ((Boolean) new FilterByAnnotation().apply(o)) {
                             if (o instanceof SootMethod) {
                                 System.out.println("------------------"+((SootMethod) o).getDeclaringClass().getName()+"."+((SootMethod) o).getName());
                                 List<FlowObject> trees = flowDiscovery((SootMethod) o);
                                 for(FlowObject tree : trees) {
                                     Item item = new ItemFactory().setFlowObject(tree).build().getItem();
                                     if (!Util.isExist((ObjectNode) discovery.get("methods"),item)) {
                                         String name = buildName(item);
                                         String json = new ObjectMapper().writeValueAsString(item);
                                         JsonNode node = new ObjectMapper().readTree(json);
                                         ((ObjectNode) discovery.get("methods")).set(name, node);
                                     }
                                     logger.debug(JDRY, "successfully process tree for " + ((SootMethod) o).getName());
                                 }
                             }
                         }
                                if (o instanceof SootClass) {
                                    ClassInfo classInfo = getClassInfo((SootClass) o);
                                    LinkedList<SootClass> list = new ClassInheritanceDiscovery().apply((SootClass) o);
                                    Item item = new ClassFactory().setClassInfo(classInfo).setRoot((SootClass)o).setSootClass(list).build().getItem();
                                    String json = new ObjectMapper().writeValueAsString(item);
                                    JsonNode node = new ObjectMapper().readTree(json);
                                    ((ObjectNode)discovery.get("classes")).set(item.getName(),node);
                                    logger.debug(JDRY,"successfully process Class for " + ((SootClass) o).getName());
                                }

                                 if (o instanceof SootField) {
                                     Item item = new FieldFactory().setSootField((SootField)o).build().getItem();
                                     String json = new ObjectMapper().writeValueAsString(item);
                                     JsonNode node = new ObjectMapper().readTree(json);
                                     ((ObjectNode)discovery.get("fields")).set(item.getNamespce()+"."+item.getName()+"_"+((SootField)o).getName(),node);
                                     logger.debug(JDRY,"successfully process Field for " + ((SootField) o).getName());
                                  }

                        if (o instanceof Entity) {
                            Item item = new EntityFactory().setEntity((Entity)o).build().getItem();
                            String json = new ObjectMapper().writeValueAsString(item);
                            JsonNode node = new ObjectMapper().readTree(json);
                            ((ObjectNode)discovery.get("entities")).set(item.getName(),node);
                            logger.debug(JDRY,"successfully process Entity for " + item.getName());
                        }

                    } catch (Throwable e) {
                        logger.error(JDRY,"fail discovery ",e);
                    }
                }
            }
        }));
        SootClass appClass = Scene.v().loadClassAndSupport(Configuration.get(Context.MAIN_CLASS).asString());
        Scene.v().setMainClass(appClass);
        Scene.v().loadNecessaryClasses();
        Main.main(args);

        return this;
    }



    private ClassInfo getClassInfo(SootClass sootClass){
        try {
            return ClassInfoBuilder.newBuilder()
                    .setAbstract(sootClass.isAbstract())
                    .setApplicationClass(sootClass.isApplicationClass())
                    .setEnum(sootClass.isEnum())
                    .setFinal(sootClass.isFinal())
                    .setInterface(sootClass.isInterface())
                    .setLibraryClass(sootClass.isLibraryClass())
                    .setJavaLibraryClass(sootClass.isJavaLibraryClass())
                    .setPrivate(sootClass.isPrivate())
                    .setProtected(sootClass.isProtected())
                    .setPublic(sootClass.isPublic())
                    .setStatic(sootClass.isStatic())
                    .setInnerClass(sootClass.isInnerClass())
                    .setSingleton(org.softauto.utils.Util.isSingleton(sootClass))
                    .setGeneric(org.softauto.utils.Util.isGeneric(sootClass))
                    .setEntity(org.softauto.utils.Util.isEntity(sootClass))
                    .build()
                    .getClassInfo();
        } catch (Exception e) {
            logger.error(JDRY,"fail geting class info ",e.getMessage());
        }
        return null;
    }

}
