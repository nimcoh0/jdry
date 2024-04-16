package org.softauto.discovery;

import org.apache.commons.lang3.ClassUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.Discover;
import org.softauto.config.Configuration;
import org.softauto.config.Context;
import org.softauto.core.Utils;
import org.softauto.filter.IFilter;
import org.softauto.handlers.*;
import org.softauto.flow.*;
import org.softauto.spel.SpEL;
import org.softauto.utils.Util;
import soot.*;
import soot.jimple.JimpleBody;
import soot.jimple.Stmt;
import soot.jimple.spark.SparkTransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.tagkit.LineNumberTag;
import soot.util.queue.QueueReader;


import java.util.*;

public class MethodTreeDiscovery implements IFlow {

    private static Logger logger = LogManager.getLogger(Discover.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    CallGraph cg ;

    IFilter filter;

    static {
        soot.options.Options.v().set_keep_line_number(true);
        soot.options.Options.v().set_whole_program(true);
        soot.options.Options.v().setPhaseOption("cg","verbose:true");
    }

    @Override
    public IFlow setFilter(IFilter filter) {
        this.filter = filter;
        return this;
    }

    @Override
    public IFlow setCallGraph(CallGraph cg) {
        this.cg = cg;
        return this;
    }

    private static SootClass loadClass(String name, boolean main) {
        SootClass c = Scene.v().loadClassAndSupport(name);
        c.setApplicationClass();
        if (main) Scene.v().setMainClass(c);
        return c;
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

    public void addResponseChain(String clazz,LinkedList<String> responseChain) {
        if(!responseChain.contains(clazz)){
            responseChain.add(clazz);
        }
    }

    public void addResponseChain(List<String> clazz,LinkedList<String> responseChain) {
        for(String c : clazz){
            addResponseChain(c,responseChain);
        }
    }

    /**
     * build method flow
     * @param o
     * @return
     */
    @Override
    public Object apply(Object o) {
        FlowObject flowObject = null;
        try {
            if(o != null) {
                SootMethod m = (SootMethod) o;
                flowObject = FlowBuilder.newBuilder().setStaticInitializer(m.isStaticInitializer()).setStatic(m.isStatic()).setConstructor(m.isConstructor()).setCg(cg).setName(m.getName()).setClazz(m.getDeclaringClass().getName()).setMethod(m).build().getFlowObject();
                List<String> unboxList = Configuration.has(org.softauto.config.Context.UNBOX_RETURN_TYPE) ? evaluateList(Configuration.get(org.softauto.config.Context.UNBOX_RETURN_TYPE).asList()) : new ArrayList<>();
                List<String> unboxExcludeList = Configuration.has(org.softauto.config.Context.UNBOX_EXCLUDE_RETURN_TYPE) ? evaluateList(Configuration.get(org.softauto.config.Context.UNBOX_EXCLUDE_RETURN_TYPE).asList()) : new ArrayList<>();
                List<String> includeDomain = Configuration.has("include_domain") ? evaluateList(Configuration.get("include_domain").asList()): new ArrayList<>();
                ReachableMethods rm = new ReachableMethods(cg, Collections.singleton(m));
                rm.update();
                QueueReader<MethodOrMethodContext> qr = rm.listener();
                HandleReturn handleReturn = new HandleReturn();
                String unboxReturnType = m.getReturnType().toString();
                LinkedList<String> responseChain = new LinkedList<>();
                String unboxReturnTypeTargetObject = null;
                flowObject.setSubsignature(m.getSubSignature());
                HashMap<SootClass,SootMethod> invokeMap = new HashMap<>();
                invokeMap.put(m.getDeclaringClass(),m);
                while (qr.hasNext() && (unboxList.contains(unboxReturnType) || unboxReturnType == null )) {
                    MethodOrMethodContext momc = qr.next();
                    if (momc != null) {

                        SootMethod  m1 = momc.method();
                        if (m1.isConcrete()) {
                            //if (Util.isInclude(m1.getDeclaringClass().getName(),includeDomain) && !m1.getDeclaringClass().getName().contains("$") && !m1.getName().contains("$")) {
                            if (!m1.getDeclaringClass().getName().contains("$") && !m1.getName().contains("$")) {
                                Body body = m1.getActiveBody();

                                handleReturn = new HandleReturn().setBody(body).setUnboxList(unboxList).setUnboxExcludeList(unboxExcludeList);
                                handleReturn.parser(unboxReturnType);
                                invokeMap.putAll(handleReturn.getInvokeMap());
                                unboxReturnType = handleReturn.getType() != null ? handleReturn.getType() : unboxReturnType;
                                addResponseChain(handleReturn.getResponseChain(), responseChain);
                                unboxReturnTypeTargetObject = unboxReturnType;
                           }
                        }
                    }
                }
                //HandleParametersParameterizedType parametersParameterizedType = new HandleParametersParameterizedType().setFlowObject(flowObject).setTags(m.getTags()).build();
                String resultParameterizedType = null;
                if(flowObject.getResultParameterizedType()== null) {
                    resultParameterizedType = new HandleParameterizedType().setFlowObject(flowObject).setTags(m.getTags()).build().getParameterizedType();
                    flowObject.setResultParameterizedType(resultParameterizedType);
                }

               // for(Map.Entry invoke : invokeMap.entrySet()){
                  //  loadClass(((SootClass)invoke.getKey()).getName(),false);
              //  }

                //SootClass c = loadClass("example.Test1",true);
                //SootClass c = m.getDeclaringClass();

                //Scene.v().loadNecessaryClasses();
               // Scene.v().setEntryPoints(EntryPoints.v().all());

                //SootClass c = Scene.v().loadClass(mainMethod.getDeclaringClass().toString(), SootClass.BODIES);
                //c.setApplicationClass();
                //SootMethod entryPoint = c.getMethod(m.getName(),m.getParameterTypes());
                //List<SootMethod> entryPoints = new ArrayList();
                //entryPoints.add(mainMethod);
                //Scene.v().setEntryPoints(entryPoints);


                /*
                List<SootMethod> entryPoints = new ArrayList();
                Map ls = new HashMap();
                SootClass c = null;
                for(Map.Entry invoke : invokeMap.entrySet()){
                    c =  ((SootClass)invoke.getKey());
                    SootMethod entryPoint = c.getMethod(((SootMethod)invoke.getValue()).getName(),((SootMethod)invoke.getValue()).getParameterTypes());
                    entryPoints.add(entryPoint);
                    loadClass(((SootClass)invoke.getKey()).getName(),false);
                    //UnitPatchingChain units = entryPoint.getActiveBody().getUnits();
                    //soot.Scene.v().loadNecessaryClasses();
                    ls.putAll(getLocals(c, ((SootMethod)invoke.getValue()).getName(), "java.lang.Integer"));

                }
                //soot.Scene.v().setEntryPoints(EntryPoints.v().all());
                Scene.v().setMainClass(c);
                Scene.v().setEntryPoints(entryPoints);
                //setSparkPointsToAnalysis();
                //printLocalIntersects(ls);
*/

/*
                HandleReturn1 handleReturn1 = new HandleReturn1();
                if(!unboxReturnType.contains(Configuration.get(Context.DOMAIN).asString()) && (flowObject.getResultParameterizedType() == null || !flowObject.getResultParameterizedType().contains(Configuration.get(Context.DOMAIN).asString()) )){
                    while (qr.hasNext() && (virtualInvokeList.size() > 0 )) {
                        MethodOrMethodContext momc = qr.next();
                        if (momc != null) {
                            SootMethod m1 = momc.method();
                            if (m1.isConcrete()) {
                                if (!m1.getDeclaringClass().getName().contains("$") && !m1.getName().contains("$")) {
                                    Body body = m1.getActiveBody();
                                    String unboxReturnType1 = unboxReturnType;
                                    if (virtualInvokeList.contains(m1.getSubSignature())) {
                                        String resultParameterizedType1 = new HandleParameterizedType().setFlowObject(flowObject).setTags(m1.getTags()).build().getParameterizedType();
                                        if(flowObject.getResultParameterizedType() == null && resultParameterizedType1 != null) {
                                            flowObject.setResultParameterizedType(resultParameterizedType1);
                                        }
                                        if(resultParameterizedType != null){
                                            unboxReturnType1 = resultParameterizedType1;
                                        }

                                        handleReturn1 = new HandleReturn1().setBody(body).setUnboxList(unboxList).setUnboxExcludeList(unboxExcludeList);
                                        unboxReturnTypeTargetObject = handleReturn1.parser(unboxReturnType1);
                                        if(unboxReturnTypeTargetObject != null){
                                            virtualInvokeList = new HashSet<>();
                                        }
                                        //HandleParametersParameterizedType parametersParameterizedType1 = new HandleParametersParameterizedType().setFlowObject(flowObject).setTags(m1.getTags()).build();

                                        //virtualInvokeList = handleReturn.getVirtualInvokeList();
                                        //unboxReturnType = handleReturn.getType() != null ? handleReturn.getType() : unboxReturnType;
                                    }
                                }
                            }
                        }
                    }
                }


 */


                Set<String> types = handleReturn.getTypes();
                Set<String> names = handleReturn.getNames();
                types.add(handleReturn.getType());
                names.add(handleReturn.getName());
                flowObject.setArgsname(getArgsName(m));
                flowObject.setParametersType(m.getParameterTypes());
                flowObject.setUnboxReturnTypeTargetObject(unboxReturnTypeTargetObject);
                HandleParametersParameterizedType parametersParameterizedType = new HandleParametersParameterizedType().setFlowObject(flowObject).setTags(m.getTags()).build();
                //if(parametersParameterizedType != null && parametersParameterizedType.getParameterizedTypes().size() > 0){
                 //   for(Map.Entry map : parametersParameterizedType.getParameterizedTypes().entrySet() ){
                  //    flowObject.getParametersType().set((Integer)map.getKey(), flowObject.getParametersType().get((Integer)map.getKey()) + "<"+map.getValue() +">");
                  //  }
                //}

                flowObject.setParametersParameterizedType(parametersParameterizedType.getParameterizedTypes());
                //flowObject.setResultParameterizedType(handleReturn.getResultParameterizedType());

                if(types.size()> 1){
                    HashMap<String,String> hm = new HashMap<>();
                    for(int i=0;i<types.size();i++){
                        if(names.toArray()[i] != null) {
                            String name = names.toArray()[i].toString();
                            if (name != null && name.startsWith("class ")) {
                                name = name.substring(name.indexOf("L") + 1, name.length() - 2).replace("/", ".");
                            }
                            hm.put(name, types.toArray()[i].toString());
                        }
                    }
                    //responseChain.add("java.util.HashMap");
                    unboxReturnType = responseChain.get(responseChain.size() -1);
                    flowObject.setReturnName(null);
                    flowObject.setUnboxReturnType(responseChain.get(responseChain.size() -1));
                    flowObject.setReturnType(m.getReturnType().toString());
                    flowObject.setResponseChain(responseChain);
                }else {

                    if (unboxReturnType != null && !responseChain.contains(unboxReturnType) && !Utils.isPrimitive(unboxReturnType)) {
                       // responseChain.add(unboxReturnType);
                    }
                    String name = handleReturn != null ? handleReturn.getName() : null;
                    if (name != null && name.startsWith("class ")) {
                        name = name.substring(name.indexOf("L") + 1, name.length() - 2).replace("/", ".");
                    }

                    flowObject.setResponseChain(responseChain);
                    if (name == null || name.contains("$stack")) {

                        flowObject.setReturnName(null);
                    } else {
                        flowObject.setReturnName(name);
                    }

                    if (unboxReturnType != null) {
                        if (unboxReturnType.contains("$")) {
                            unboxReturnType = unboxReturnType.replace("$", ".");
                        }
                        flowObject.setUnboxReturnType(unboxReturnType);
                        flowObject.setReturnType(m.getReturnType().toString());
                    }
                }

                //flowObject.setArgsname(getArgsName(m));
                HandleGenericDiscovery handleGenericDiscovery = new HandleGenericDiscovery().setFlowObject(flowObject).setTags(m.getTags()).build();
                if (handleGenericDiscovery.getGlobalGeneric() != null && handleGenericDiscovery.getGlobalGeneric().equals(unboxReturnType)) {
                    flowObject.setReturnTypeGeneric(true);
                }
                //resultParameterizedType = new HandleParameterizedType().setFlowObject(flowObject).setTags(m.getTags()).build().getParameterizedType();
                //if (flowObject.getResultParameterizedType() == null && resultParameterizedType != null) {
                   // unboxReturnType = unboxReturnType +"<" + resultParameterizedType + ">";
                   // flowObject.setUnboxReturnType(unboxReturnType);
                   // flowObject.setResultParameterizedType(resultParameterizedType);
               // }
                updateReturnTypeFromRequest(flowObject);
                logger.debug(JDRY, "build object flow for " + ((SootMethod) o).getName());
            }
        } catch (Exception e) {
           logger.error(JDRY,"fail build object flow for "+o.getClass().getTypeName());
        }
        return flowObject;
    }




    private void updateReturnTypeFromRequest(FlowObject flowObject ){
       for(int i=0;i<flowObject.getArgsname().size();i++){
            if(flowObject.getReturnName() != null && flowObject.getReturnName().equals(flowObject.getArgsname().get(i))){
                flowObject.setUnboxReturnType(flowObject.getMethod().getParameterType(i).toString());
            }
        }
    }


    @Override
    public String getName() {
        return this.getClass().getTypeName();
    }


    /**
     * set method arguments names as list
     * @param m
     * @return
     */
    public  List<String> getArgsName(SootMethod m ){
        List<String> names = new ArrayList<>();
        if(m.hasActiveBody()) {
            List<Local> locals = m.getActiveBody().getParameterLocals();
            for (Local local : locals) {
                names.add(local.getName());
            }
        }
        return names;

    }

    static void setSparkPointsToAnalysis() {
        System.out.println("[spark] Starting analysis ...");

        HashMap opt = new HashMap();
        opt.put("enabled","true");
        opt.put("verbose","true");
        opt.put("ignore-types","false");
        opt.put("force-gc","false");
        opt.put("pre-jimplify","false");
        opt.put("vta","false");
        opt.put("rta","false");
        opt.put("field-based","false");
        opt.put("types-for-sites","false");
        opt.put("merge-stringbuffer","true");
        opt.put("string-constants","false");
        opt.put("simulate-natives","true");
        opt.put("simple-edges-bidirectional","false");
        opt.put("on-fly-cg","true");
        opt.put("simplify-offline","false");
        opt.put("simplify-sccs","false");
        opt.put("ignore-types-for-sccs","false");
        opt.put("propagator","worklist");
        opt.put("set-impl","double");
        opt.put("double-set-old","hybrid");
        opt.put("double-set-new","hybrid");
        opt.put("dump-html","false");
        opt.put("dump-pag","false");
        opt.put("dump-solution","false");
        opt.put("topo-sort","false");
        opt.put("dump-types","true");
        opt.put("class-method-var","true");
        opt.put("dump-answer","false");
        opt.put("add-tags","false");
        opt.put("set-mass","false");

        SparkTransformer.v().transform("",opt);

        System.out.println("[spark] Done!");
    }

    private static int getLineNumber(Stmt s) {
        Iterator ti = s.getTags().iterator();
        while (ti.hasNext()) {
            Object o = ti.next();
            if (o instanceof LineNumberTag)
                return Integer.parseInt(o.toString());
        }
        return -1;
    }

    private static Map/*<Integer,Local>*/ getLocals(SootClass sc, String methodname, String typename) {
        Map res = new HashMap();
        try {
            Iterator mi = sc.getMethods().iterator();
            while (mi.hasNext()) {
                SootMethod sm = (SootMethod)mi.next();
                System.err.println(sm.getName());
                if (true && sm.getName().equals(methodname) && sm.isConcrete()) {
                    //if (true && sm.isConcrete()) {
                    JimpleBody jb = (JimpleBody)sm.retrieveActiveBody();
                    Iterator ui = jb.getUnits().iterator();
                    while (ui.hasNext()) {
                        Stmt s = (Stmt)ui.next();
                        int line = getLineNumber(s);
                        // find definitions
                        Iterator bi = s.getDefBoxes().iterator();
                        while (bi.hasNext()) {
                            Object o = bi.next();
                            if (o instanceof ValueBox) {
                                Value v = ((ValueBox)o).getValue();
                                String type = v.getType().toString();
                                //if(Utils.isPrimitive(type)){
                                 //   Class c = ClassUtils.getClass(type);
                                 //   type = ClassUtils.primitiveToWrapper(c).getTypeName();
                                //}
                                if (type.equals(typename) && v instanceof Local)
                                    //if ( v instanceof Local)
                                    res.put(new Integer(line),v);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    private static void printLocalIntersects(Map/*<Integer,Local>*/ ls) {
        soot.PointsToAnalysis pta = Scene.v().getPointsToAnalysis();
        Iterator i1 = ls.entrySet().iterator();
        while (i1.hasNext()) {
            Map.Entry e1 = (Map.Entry)i1.next();
            int p1 = ((Integer)e1.getKey()).intValue();
            Local l1 = (Local)e1.getValue();
            PointsToSet r1 = pta.reachingObjects(l1);

            Iterator i2 = ls.entrySet().iterator();
            while (i2.hasNext()) {
                Map.Entry e2 = (Map.Entry)i2.next();
                int p2 = ((Integer)e2.getKey()).intValue();
                Local l2 = (Local)e2.getValue();
                PointsToSet r2 = pta.reachingObjects(l2);
                if (p1<=p2)
                    System.out.println("["+p1+","+p2+"]\t Container intersect? "
                            +r1.hasNonEmptyIntersection(r2));
            }
        }
    }
}
