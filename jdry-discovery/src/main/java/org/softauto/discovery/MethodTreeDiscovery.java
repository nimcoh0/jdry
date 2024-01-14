package org.softauto.discovery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.Discover;
import org.softauto.config.Context;
import org.softauto.config.Configuration;
import org.softauto.core.Utils;
import org.softauto.filter.FilterByAnnotation;
import org.softauto.filter.IFilter;
import org.softauto.clazz.ClassInfo;
import org.softauto.clazz.ClassInfoBuilder;
import org.softauto.handlers.HandleReturn;
import org.softauto.spel.SpEL;
import org.softauto.flow.*;
import soot.*;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.Targets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MethodTreeDiscovery implements IFlow {

    private static Logger logger = LogManager.getLogger(Discover.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    CallGraph cg ;

    IFilter filter;

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

    private List<String> evaluateList(List<String> beforeEvaluateList){
        List<String> afterEvaluateList = new ArrayList<>();
        for(String str : beforeEvaluateList){
            Object o = SpEL.getInstance().evaluate(str);
            if(o != null)
            afterEvaluateList.add(o.toString());
        }
        return afterEvaluateList;
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
            SootMethod m = (SootMethod) o;
            SpEL.getInstance().addProperty("method",m);

            flowObject =  FlowBuilder.newBuilder().setStaticInitializer(m.isStaticInitializer()).setStatic(m.isStatic()).setConstructor(m.isConstructor()).setClassInfo(getClassInfo(m)).setCg(cg).setName(m.getName()).setClazz(m.getDeclaringClass().getName()).setMethod(m).build().getFlowObject();
            List<String> unboxList = Configuration.has(Context.UNBOX_RETURN_TYPE) ? evaluateList(Configuration.get(Context.UNBOX_RETURN_TYPE).asList()): null;
            List<String> unboxExcludeList = Configuration.has(Context.UNBOX_EXCLUDE_RETURN_TYPE) ? evaluateList(Configuration.get(Context.UNBOX_EXCLUDE_RETURN_TYPE).asList()): null;
            String unboxReturnType = m.getReturnType().toString();
            LinkedList<String> responseChain = new LinkedList<>();
            HandleReturn handleReturn = null;
            Iterator<MethodOrMethodContext> iter = cg.sourceMethods();
            while (iter.hasNext() && unboxList.contains(unboxReturnType)) {
                MethodOrMethodContext methodOrMethodContext = iter.next();
                Body body = methodOrMethodContext.method().getActiveBody();
                //returnType = edge.getSrc().method().getReturnType().toString();
                //while (unboxList.contains(returnType)) {
                    handleReturn = new HandleReturn().setBody(body).setUnboxList(unboxList).setUnboxExcludeList(unboxExcludeList);
                    handleReturn.parser(unboxReturnType);
                    unboxReturnType = handleReturn.getType();
                    responseChain.addAll(handleReturn.getResponseChain());
            }
                    String name = handleReturn != null ? handleReturn.getName() : null;
                    flowObject.setResponseChain(responseChain);
                    if (name == null || name.contains("$stack")) {
                        //flowObject.setReturnTypeName(Utils.getShortName(m.getReturnType().toString().toLowerCase()));
                        flowObject.setReturnTypeName(null);
                    } else {
                        flowObject.setReturnTypeName(name);
                    }
                    if (unboxReturnType != null) {
                        if (unboxReturnType.contains("$")) {
                            unboxReturnType = unboxReturnType.replace("$", ".");
                        }
                        flowObject.setUnboxReturnType(unboxReturnType);
                        flowObject.setReturnType(m.getReturnType().toString());
                    }


            List<Integer> use = new ArrayList<>();
            buildTree(m,cg,flowObject,use);
            flowObject.setArgsname(getArgsName(m));
            logger.debug(JDRY,"build object flow for " + ((SootMethod) o).getName() );
        } catch (Exception e) {
           logger.error(JDRY,"fail build object flow for "+o.getClass().getTypeName());
        }
        return flowObject;
    }




    @Override
    public String getName() {
        return this.getClass().getTypeName();
    }

    /**
     * build method Call Graph
     * @param sootMethod
     * @param cg
     * @param flowObject
     * @param use
     * @return
     */
    private FlowObject buildTree(SootMethod sootMethod, CallGraph cg,FlowObject flowObject,List<Integer> use){
        try {
            List<SootMethod> sootMethods = getChildes(sootMethod,cg);
            for(SootMethod method : sootMethods) {
                if ((Boolean) new FilterByAnnotation().apply(method)) {
                    if (!use.contains(method.getNumber())) {
                        use.add(method.getNumber());
                        FlowObject flowObject1 = FlowBuilder.newBuilder().setClassInfo(getClassInfo(sootMethod)).setCg(cg).setName(method.getName()).setClazz(method.getDeclaringClass().getName()).setMethod(method).build().getFlowObject();
                        flowObject1.setArgsname(getArgsName(method));
                        buildTree(method, cg, flowObject1, use);
                        if (flowObject1.getName().equals("<init>")) {
                            flowObject1.setReturnType(flowObject1.getClazz());
                        } else {
                            flowObject1.setReturnType(flowObject1.getMethod().getReturnType().toString());
                        }
                        flowObject.addChiled(flowObject1);
                    }
                }
                flowObject.setArgsname(getArgsName(flowObject.getMethod()));
                logger.debug(JDRY,"successfully build CallGraph for " + sootMethod.getName());
            }
        } catch (Exception e) {
            logger.error(JDRY,"fail build method CallGraph for "+ sootMethod.getName());
        }
        return flowObject;
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


    /**
     * set class info
     * @param sootMethod
     * @return
     */
    private ClassInfo getClassInfo(SootMethod sootMethod){
        try {
            SootClass sootClass = sootMethod.getDeclaringClass();
            boolean hasParameters = false;
            if(sootMethod.isConstructor()){
                if(sootMethod.getParameterCount() > 0){
                    hasParameters = true;
                }
            }else {
                hasParameters = true;
                for(SootMethod sootMethod1 : sootClass.getMethods()){
                    if(sootMethod1.isConstructor()){
                        if(sootMethod.getParameterCount() == 0){
                            hasParameters = false;
                        }
                    }
                }
            }

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
                    .setHasParameters(hasParameters)
                    .build()
                    .getClassInfo();
        } catch (Exception e) {
           logger.error(JDRY,"fail geting class info ",e.getMessage());
        }
        return null;
    }

    public  List<SootMethod> getChildes(SootMethod sootMethod, CallGraph cg){
        List<SootMethod> sootMethods = new ArrayList<>();
        try {
            Iterator<MethodOrMethodContext> targets = new Targets(cg.edgesOutOf(sootMethod));
            while (targets.hasNext()) {
                SootMethod tgt = (SootMethod)targets.next();
                if(tgt.getDeclaringClass().toString().contains( Configuration.get(Context.DOMAIN).asString())) {
                    sootMethods.add(tgt);
                }
            }
            logger.debug(JDRY,"successfully got childes for " + sootMethod);
        } catch (Exception e) {
            logger.error(JDRY,"fail childes discovery for "+sootMethod,e.getMessage() );
        }
        return sootMethods;
    }
}
