package org.softauto.discovery;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.Discover;
import org.softauto.config.Configuration;
import org.softauto.config.Context;
import org.softauto.discovery.filter.Filter;
import org.softauto.discovery.filter.FilterByAnnotation;
import org.softauto.discovery.filter.FilterByDomain;
import org.softauto.discovery.handlers.HandleFlowDiscovery;
import org.softauto.discovery.handlers.HandleMethodDiscovery;
import org.softauto.discovery.handlers.flow.ClassInheritanceDiscovery;
import org.softauto.discovery.handlers.flow.FlowObject;
import org.softauto.discovery.handlers.flow.MethodTreeDiscovery;
import org.softauto.discovery.handlers.method.DiscoveryByAnnotation;
import org.softauto.espl.Espl;
import org.softauto.handlers.HandelClassAnnotation;
import org.softauto.model.item.*;
import soot.*;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.ReachableMethods;

import soot.util.queue.QueueReader;

import java.util.*;

public class Discovery extends AbstractDiscovery {

    private static Logger logger = LogManager.getLogger(Discover.class);

    private static HashMap<String,Integer> nameCounter = new HashMap<>();

    protected FlowObject flowDiscovery(SootMethod mainMethod){
        FlowObject flowObject = null;
        try {
            logger.debug("flow  discovery for " + mainMethod );
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
                Espl.getInstance().addProperty("method",m);
                while (qr.hasNext()) {
                    MethodOrMethodContext momc = qr.next();
                    if (momc != null) {
                        m = momc.method();
                        if (m.isConcrete()) {
                            if (Filter.filter(new FilterByDomain().set(Configuration.get(Context.DOMAIN).asString()), m.getDeclaringClass())) {
                                m.getActiveBody().getParameterLocals();
                                return    HandleFlowDiscovery.handleFlowDiscovery(new MethodTreeDiscovery().setCallGraph(cg).setFilter(new FilterByDomain().set(Configuration.get(Context.DOMAIN).asString())), m);
                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
            logger.error("fail flow Discovery ",e.getMessage());
        }


        return flowObject ;
    }

    protected List<Object> discovery(){
        List<Object> sootItems = new ArrayList<>();
        try {
            CHATransformer.v().transform();
            CallGraph cg = Scene.v().getCallGraph();

            /*
            File file = new File("c:/tmp/sample.txt");
            //Instantiating the PrintStream class
            PrintStream stream = new PrintStream(file);
            System.out.println("From now on "+file.getAbsolutePath()+" will be your console");
            System.setOut(stream);


             */
            for(SootClass sc : Scene.v().getApplicationClasses()) {
                if (Filter.filter(new FilterByDomain().set(Configuration.get(Context.DOMAIN).asString()), sc)) {
                    Espl.getInstance().addProperty("class",sc);
                    if(sc.getModifiers() > 0) {
                        if (!isInClazzList(sc.getName())) {
                            clazzes.add(sc.getName());
                            //for (SootField f : sc.getFields()) {
                             //   SootField sootField = HandleFiledDiscovery.filedDiscovery(new DiscoveryFieldByAnnotation().set(Configuration.get(Context.DISCOVER_FIELD_BY_ANNOTATION).asList()), f);
                             //   if (sootField != null) {
                              //      sootItems.add(sootField);
                              //  }
                            //}
                        }
                    }
                    for (SootMethod m : sc.getMethods()) {
                        Espl.getInstance().addProperty("method",m);
                        SootMethod sootMethod = HandleMethodDiscovery.handleMethodDiscovery(new DiscoveryByAnnotation().set(Configuration.get(Context.DISCOVER_BY_ANNOTATION).asList()), m);
                        if(sootMethod != null){
                            sootItems.add(sootMethod);
                        }
                    }
                    HashMap<String, Object> clazzMap =  new HandelClassAnnotation(sc).analyze();
                    if(clazzMap != null && clazzMap.size() >0 ){
                        sootItems.add(sc);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("fail method Discovery ",e.getMessage());
        }
        return sootItems;
    }

    private static String buildName(Item item){
        String name = null;
        if(nameCounter.containsKey(item.getNamespce()+"."+item.getName())){
            Integer i = nameCounter.get(item.getNamespce() + "." + item.getName());
            name = item.getNamespce() + "." + item.getName() + "_" +i;
            nameCounter.put(item.getNamespce()+"."+item.getName(),i++);
        }else {
            nameCounter.put(item.getNamespce()+"."+item.getName(),0);
            name = item.getNamespce() + "." + item.getName();
        }
        return name;
    }

    public static boolean isExist(Item item, JsonNode items){
        try {
            if(!items.has(item.getNamespce()+"."+item.getName())){
                return false;
            }
            for(JsonNode node : items){
                if (node.get("namespce").equals(item.getNamespce()) && node.get("name").equals(item.getName())){
                        if(node.get("parametersTypes").equals(item.getParametersTypes())){
                            return true;
                        }else {
                          return false;
                        }
                    }
            }
            logger.debug("item not found " +item.getNamespce()+ "."+item.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Discovery discover(){

        PackManager.v().getPack("wjtp").add(new Transform("wjtp.myTrans", new SceneTransformer() {

            @Override
            protected void internalTransform(String phaseName, Map options) {

                List<Object> sootItems = discovery();
                logger.debug("successfully discover " + sootItems.size() + " sootItems");

                discovery.put("discover",Configuration.get(Context.FILE_NAME).asString());
                discovery.put("namespace",Configuration.get(Context.FILE_NAMESPACE).asString());
                discovery.put("version",Configuration.get(Context.VERSION).asString());
                discovery.put("doc",Configuration.get(Context.DOC).asString());
                discovery.set("methods",new ObjectMapper().createObjectNode());
                discovery.set("classes",new ObjectMapper().createObjectNode());
                for(Object o : sootItems){
                    try {
                         if ((Boolean) new FilterByAnnotation().apply(o)) {
                                if (o instanceof SootMethod) {
                                   FlowObject tree = flowDiscovery((SootMethod) o);
                                   Item item =  new ItemFactory().setFlowObject(tree).build().getItem();
                                   if(!isExist(item,discovery.get("methods"))) {
                                       String name = buildName(item);
                                       String json = new ObjectMapper().writeValueAsString(item);
                                       JsonNode node = new ObjectMapper().readTree(json);
                                       ((ObjectNode)discovery.get("methods")).set(name,node);
                                    }
                                    logger.debug("successfully process tree for " + ((SootMethod) o).getName());
                                }
                                if (o instanceof SootClass) {
                                    LinkedList<SootClass> list = new ClassInheritanceDiscovery().apply((SootClass) o);
                                    Item item = new ClassFactory().setRoot((SootClass)o).setSootClass(list).build().getItem();
                                    String json = new ObjectMapper().writeValueAsString(item);
                                    JsonNode node = new ObjectMapper().readTree(json);
                                    ((ObjectNode)discovery.get("classes")).set(item.getName(),node);
                                    logger.debug("successfully process Class for " + ((SootClass) o).getName());
                                }

                            }
                    } catch (Exception e) {
                        logger.error("fail discovery for "+ ((SootMethod)o).getName(),e);
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




}
