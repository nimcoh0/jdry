package org.softauto.discovery;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.ClassUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.Discover;
import org.softauto.config.Context;
import org.softauto.config.Configuration;
import org.softauto.handlers.HandleFlowDiscovery;
import org.softauto.flow.FlowObject;
import org.softauto.handlers.HandleMethodDiscovery;
import org.softauto.model.clazz.ClassFactory;
import org.softauto.model.item.*;
import org.softauto.spel.SpEL;
import org.softauto.utils.Util;
import soot.*;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInstanceFieldRef;
import soot.jimple.internal.JimpleLocal;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.jimple.toolkits.pointer.LocalMustNotAliasAnalysis;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.util.queue.QueueReader;
import java.util.*;



public class Discovery extends AbstractDiscovery {

    private static Logger logger = LogManager.getLogger(Discover.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    private static HashMap<String,Integer> nameCounter = new HashMap<>();




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
    List<String> includeDomain = Configuration.has("include_domain") ? evaluateList(Configuration.get("include_domain").asList()): new ArrayList<>();


    protected List<FlowObject> flowDiscovery(SootMethod mainMethod){
        List<FlowObject> flowObjects = new ArrayList<>();
        try {

            logger.debug(JDRY,"flow  discovery for " + mainMethod );
            //SootClass c = Scene.v().loadClass(mainMethod.getDeclaringClass().toString(), SootClass.BODIES);
            //c.setApplicationClass();
            //SootMethod entryPoint = c.getMethod(mainMethod.getName(),mainMethod.getParameterTypes());
            List<SootMethod> entryPoints = new ArrayList();
            entryPoints.add(mainMethod);
            Scene.v().setEntryPoints(entryPoints);
           // Scene.v().loadNecessaryClasses();
            CHATransformer.v().transform();
            CallGraph cg = Scene.v().getCallGraph();
            //List<SootMethod> lm = Scene.v().getEntryPoints();
            //for (SootMethod em: lm) {
               // ReachableMethods rm;
               // rm = new ReachableMethods(cg, Collections.singleton(em));
               // rm.update();
               // QueueReader<MethodOrMethodContext> qr = rm.listener();
                //SootMethod m = null;
               // while (qr.hasNext()) {
                   // MethodOrMethodContext momc = qr.next();
                    //if (momc != null) {
                       // m = momc.method();
                       // if (m.isConcrete()) {
                            if (Util.isInclude(mainMethod.getDeclaringClass().getName(),includeDomain) && !mainMethod.getDeclaringClass().getName().contains("$") && !mainMethod.getName().contains("$")){
                                FlowObject flowObject = HandleFlowDiscovery.handleFlowDiscovery(new MethodTreeDiscovery().setCallGraph(cg), HandleMethodDiscovery.handleMethodDiscovery(new DiscoveryByAnnotation().set(Configuration.get(Context.DISCOVER_BY_ANNOTATION).asList()),mainMethod));
                                if(flowObject != null){
                                    flowObjects.add(flowObject);
                                }
                            }
                       // }

                   // }
                //}
            //}
        } catch (Exception e) {
            logger.error(JDRY,"fail flow Discovery ",e.getMessage());
        }
        return flowObjects;
    }



    protected void discovery(){
        try {
            CHATransformer.v().transform();
            CallGraph cg = Scene.v().getCallGraph();
            for(SootClass sc : Scene.v().getApplicationClasses()) {
                if (Util.isInclude(sc.getName(),includeDomain)){
                    for (SootMethod m : sc.getMethods()) {
                        if(!m.getName().contains("$")) {
                            SootMethod sootMethod = HandleMethodDiscovery.handleMethodDiscovery(new DiscoveryByAnnotation().set(Configuration.get(Context.DISCOVER_BY_ANNOTATION).asList()), m);
                            if(sootMethod != null) {
                                List<FlowObject> trees = flowDiscovery((SootMethod) sootMethod);
                                for (FlowObject tree : trees) {
                                    Item item = new ItemFactory().setFlowObject(tree).build().getItem();
                                    if (!Util.isExist((ObjectNode) discovery.get("methods"), item)) {
                                        String name = buildName(item);
                                        item.setFullname(name);
                                        String json = new ObjectMapper().writeValueAsString(item);
                                        JsonNode node = new ObjectMapper().readTree(json);
                                        ((ObjectNode) discovery.get("methods")).set(name, node);
                                    }
                                    logger.debug(JDRY, "successfully process tree for " + ((SootMethod) m).getName());
                                }
                            }
                        }
                    }

                    if(!sc.getName().contains("$")) {
                        LinkedList<SootClass> list = new ClassInheritanceDiscovery().apply(sc);
                        Item item = new ClassFactory().setRoot(sc).setSootClass(list).build().getItem();
                        if(item != null) {
                            String json = new ObjectMapper().writeValueAsString(item);
                            JsonNode node = new ObjectMapper().readTree(json);
                            ((ObjectNode) discovery.get("classes")).set(item.getName(), node);

                            logger.debug(JDRY, "successfully process Class for " + (sc).getName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(JDRY,"fail method Discovery ",e.getMessage());
        }
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

        PackManager.v().getPack("jtp").add(
                new Transform("jtp.myTransform", new BodyTransformer() {

                    protected void internalTransform(Body body, String phase, Map options) {

                        try {


                            if(body.getMethod().getDeclaringClass().getName().contains("Test1")) {
                                for (Unit unit : body.getUnits()) {
                                    for (ValueBox valueBox : unit.getUseAndDefBoxes()) {
                                        if(valueBox.getValue() instanceof JInstanceFieldRef){
                                            Class c = ClassUtils.getClass(((JInstanceFieldRef) valueBox.getValue()).getFieldRef().type().toString());
                                            if(c.isPrimitive()){
                                                //Singletons.Global g = new soot.Singletons.Global();
                                                RefType refType = RefType.v("java.lang.Integer");
                                                //JimpleLocal local = new JimpleLocal(((JInstanceFieldRef) valueBox.getValue()).getFieldRef().name(),((JInstanceFieldRef) valueBox.getValue()).getFieldRef().type());
                                                JimpleLocal local = new JimpleLocal(((JInstanceFieldRef) valueBox.getValue()).getFieldRef().name(),refType);
                                                valueBox.setValue(local);
                                                body.getLocals().add(local);
                                                //JimpleLocal local1 = new JimpleLocal(((JAssignStmt.LinkedRValueBox) valueBox.getValue().)..getFieldRef().name(),((JInstanceFieldRef) valueBox.getValue()).getFieldRef().type());
                                                //valueBox.setOtherBox(valueBox);
                                                //Class c1 = ClassUtils.primitiveToWrapper(c);
                                                //SootFieldRef sootFieldRef = new JInstanceFieldRef();
                                                //((JInstanceFieldRef) valueBox.getValue())..getFieldRef()..type().
                                            }

                                        }
                                        if(valueBox instanceof JAssignStmt.LinkedVariableBox){
                                            //((JAssignStmt.LinkedVariableBox) valueBox).setOtherBox( valueBox);
                                        }


                                        String s = valueBox.getValue().getType().toString();
                                        System.out.println(s);
                                    }
                                }
                            }



                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }));


        PackManager.v().getPack("wjtp").add(new Transform("wjtp.myTrans", new SceneTransformer() {


            @Override
            protected void internalTransform(String phaseName, Map options) {
                discovery.put("name",Configuration.get(Context.FILE_NAME).asString());
                discovery.put("namespace",Configuration.get(Context.FILE_NAMESPACE).asString());
                discovery.put("version",Configuration.get(Context.VERSION).asString());
                discovery.put("doc",Configuration.get(Context.DOC).asString());
                discovery.set("methods",new ObjectMapper().createObjectNode());
                discovery.set("classes",new ObjectMapper().createObjectNode());
                discovery();
        }}));
        SootClass appClass = Scene.v().loadClassAndSupport(Configuration.get(Context.MAIN_CLASS).asString());
        Scene.v().setMainClass(appClass);
        Scene.v().loadNecessaryClasses();
        Main.main(args);

        return this;
    }

}
