package org.softauto.discovery;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.config.Configuration;
import org.softauto.config.Context;
import org.softauto.discovery.filter.Filter;
import org.softauto.discovery.filter.FilterByAnnotation;
import org.softauto.discovery.filter.FilterByDomain;
import org.softauto.discovery.handlers.HandleFiledDiscovery;
import org.softauto.discovery.handlers.HandleFlowDiscovery;
import org.softauto.discovery.handlers.HandleMethodDiscovery;
import org.softauto.discovery.handlers.field.DiscoveryFieldByAnnotation;
import org.softauto.discovery.handlers.flow.ClassInheritanceDiscovery;
import org.softauto.discovery.handlers.flow.FlowObject;
import org.softauto.discovery.handlers.flow.MethodTreeDiscovery;
import org.softauto.discovery.handlers.method.DiscoveryByAnnotation;
import org.softauto.espl.Espl;
import org.softauto.handlers.HandelClassAnnotation;
import org.softauto.model.item.*;
import org.softauto.utils.Util;
import soot.*;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.jimple.toolkits.ide.libsumm.FixedMethods;

import soot.options.Options;
import soot.util.queue.QueueReader;

import java.io.File;
import java.io.PrintStream;
import java.util.*;

public class Discovery extends AbstractDiscovery {

    private static Logger logger = LogManager.getLogger(org.softauto.Main.class);


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



    public Discovery discover(){

        PackManager.v().getPack("wjtp").add(new Transform("wjtp.myTrans", new SceneTransformer() {

            @Override
            protected void internalTransform(String phaseName, Map options) {

                List<Object> sootItems = discovery();
                logger.debug("successfully discover " + sootItems.size() + " sootItems");


                for(Object o : sootItems){
                    try {
                         if ((Boolean) new FilterByAnnotation().apply(o)) {
                                if (o instanceof SootMethod) {
                                   FlowObject tree = flowDiscovery((SootMethod) o);
                                   Item item =  new ItemFactory().setFlowObject(tree).build().getItem();
                                    if(!Util.isExist(item,items)) {
                                        items.add(item);
                                    }
                                    logger.debug("successfully process tree for " + ((SootMethod) o).getName());
                                }
                                //if (o instanceof SootField) {
                                //    Item item = new FieldFactory().setSootField((SootField)o).build().getItem();
                                 //   items.add(item);
                                 //   logger.debug("successfully process Field for " + ((SootField) o).getName());
                               // }
                                if (o instanceof SootClass) {
                                    LinkedList<SootClass> list = new ClassInheritanceDiscovery().apply((SootClass) o);
                                    //for(SootClass sootClass : list){
                                     //   Item item = new ClassFactory().setSootClass(sootClass).build().getItem();
                                     //   items.add(item);
                                    //}

                                    Item item = new ClassFactory().setSootClass(list).build().getItem();
                                    items.add(item);
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
