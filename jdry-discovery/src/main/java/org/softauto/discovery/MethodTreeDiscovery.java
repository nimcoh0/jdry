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
                //List<String> unboxList = Configuration.has(org.softauto.config.Context.UNBOX_RETURN_TYPE) ? evaluateList(Configuration.get(org.softauto.config.Context.UNBOX_RETURN_TYPE).asList()) : new ArrayList<>();
                //List<String> unboxExcludeList = Configuration.has(org.softauto.config.Context.UNBOX_EXCLUDE_RETURN_TYPE) ? evaluateList(Configuration.get(org.softauto.config.Context.UNBOX_EXCLUDE_RETURN_TYPE).asList()) : new ArrayList<>();
                //List<String> includeDomain = Configuration.has("include_domain") ? evaluateList(Configuration.get("include_domain").asList()): new ArrayList<>();
                ReachableMethods rm = new ReachableMethods(cg, Collections.singleton(m));
                rm.update();
                QueueReader<MethodOrMethodContext> qr = rm.listener();
                //HandleReturn handleReturn = new HandleReturn();
                //String unboxReturnType = m.getReturnType().toString();
                LinkedList<String> responseChain = new LinkedList<>();
                String unboxReturnTypeTargetObject = null;
                flowObject.setSubsignature(m.getSubSignature());
                HashMap<SootClass,SootMethod> invokeMap = new HashMap<>();
                invokeMap.put(m.getDeclaringClass(),m);
                /*
                while (qr.hasNext() && (unboxList.contains(unboxReturnType) || unboxReturnType == null )) {
                    MethodOrMethodContext momc = qr.next();
                    if (momc != null) {

                        SootMethod  m1 = momc.method();
                        if (m1.isConcrete()) {
                            //if (Util.isInclude(m1.getDeclaringClass().getName(),includeDomain) && !m1.getDeclaringClass().getName().contains("$") && !m1.getName().contains("$")) {
                            if (!m1.getDeclaringClass().getName().contains("$") && !m1.getName().contains("$")) {
                                Body body = m1.getActiveBody();

                                //handleReturn = new HandleReturn().setBody(body).setUnboxList(unboxList).setUnboxExcludeList(unboxExcludeList);
                                //handleReturn.parser(unboxReturnType);
                                invokeMap.putAll(handleReturn.getInvokeMap());
                                //unboxReturnType = handleReturn.getType() != null ? handleReturn.getType() : unboxReturnType;
                                addResponseChain(handleReturn.getResponseChain(), responseChain);
                                //unboxReturnTypeTargetObject = unboxReturnType;
                           }
                        }
                    }
                }

                 */
                //HandleParametersParameterizedType parametersParameterizedType = new HandleParametersParameterizedType().setFlowObject(flowObject).setTags(m.getTags()).build();
                String resultParameterizedType = null;
                if(flowObject.getResultParameterizedType()== null) {
                    resultParameterizedType = new HandleParameterizedType().setFlowObject(flowObject).setTags(m.getTags()).build().getParameterizedType();
                    flowObject.setResultParameterizedType(resultParameterizedType);
                }




               // Set<String> types = handleReturn.getTypes();
               // Set<String> names = handleReturn.getNames();
               // types.add(handleReturn.getType());
               // names.add(handleReturn.getName());
                flowObject.setArgsname(getArgsName(m));
                flowObject.setParametersType(m.getParameterTypes());
                //flowObject.setUnboxReturnTypeTargetObject(unboxReturnTypeTargetObject);
                HandleParametersParameterizedType parametersParameterizedType = new HandleParametersParameterizedType().setFlowObject(flowObject).setTags(m.getTags()).build();


                flowObject.setParametersParameterizedType(parametersParameterizedType.getParameterizedTypes());

/*
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


 */
                    //unboxReturnType = responseChain.get(responseChain.size() -1);
                    //flowObject.setReturnName(null);
                    //flowObject.setUnboxReturnType(responseChain.get(responseChain.size() -1));
                    flowObject.setReturnType(m.getReturnType().toString());
                    flowObject.setResponseChain(responseChain);
               // }
            //else {

/*
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

 */
               // }


                HandleGenericDiscovery handleGenericDiscovery = new HandleGenericDiscovery().setFlowObject(flowObject).setTags(m.getTags()).build();
                if (handleGenericDiscovery.getGlobalGeneric() != null ){//&& handleGenericDiscovery.getGlobalGeneric().equals(unboxReturnType)) {
                    flowObject.setReturnTypeGeneric(true);
                }

               // updateReturnTypeFromRequest(flowObject);
                logger.debug(JDRY, "build object flow for " + ((SootMethod) o).getName());
            }
        } catch (Exception e) {
           logger.error(JDRY,"fail build object flow for "+o.getClass().getTypeName());
        }
        return flowObject;
    }



    /*

    private void updateReturnTypeFromRequest(FlowObject flowObject ){
       for(int i=0;i<flowObject.getArgsname().size();i++){
            if(flowObject.getReturnName() != null && flowObject.getReturnName().equals(flowObject.getArgsname().get(i))){
                flowObject.setUnboxReturnType(flowObject.getMethod().getParameterType(i).toString());
            }
        }
    }

     */


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



}
