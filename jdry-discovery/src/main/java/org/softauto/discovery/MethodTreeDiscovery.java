package org.softauto.discovery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.Discover;
import org.softauto.config.Context;
import org.softauto.core.Configuration;
import org.softauto.filter.FilterByAnnotation;
import org.softauto.filter.IFilter;
import org.softauto.clazz.ClassInfo;
import org.softauto.clazz.ClassInfoBuilder;
import org.softauto.handlers.HandleReturnType;
import org.softauto.espl.Espl;
import org.softauto.flow.*;
import soot.*;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Targets;
import soot.tagkit.ParamNamesTag;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MethodTreeDiscovery implements IFlow {

    private static Logger logger = LogManager.getLogger(Discover.class);

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
            Object o = Espl.getInstance().evaluate(str);
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
            Espl.getInstance().addProperty("method",m);

            flowObject =  FlowBuilder.newBuilder().setStaticInitializer(m.isStaticInitializer()).setStatic(m.isStatic()).setConstructor(m.isConstructor()).setClassInfo(getClassInfo(m)).setCg(cg).setName(m.getName()).setClazz(m.getDeclaringClass().getName()).setMethod(m).build().getFlowObject();
            //flowObject.setEntity(HandleEntity.isEntity(m.getDeclaringClass().getName()));
            //if(Configuration.has("unbox_return_type") && Configuration.get("unbox_return_type").asList().contains(m.getReturnType().toString())) {
            List<String> tags = null;
            if(m.hasTag(ParamNamesTag.NAME)){
                tags = ((ParamNamesTag) m.getTag(ParamNamesTag.NAME)).getInfo();
                //tags = m.getTag(ParamNamesTag.NAME);
            }
            List<String> unboxList = Configuration.has(Context.UNBOX_RETURN_TYPE) ? evaluateList(Configuration.get(Context.UNBOX_RETURN_TYPE).asList()): null;
            List<String> unboxExcludeList = Configuration.has(Context.UNBOX_EXCLUDE_RETURN_TYPE) ? evaluateList(Configuration.get(Context.UNBOX_EXCLUDE_RETURN_TYPE).asList()): null;
            if(unboxList.contains(m.getReturnType().toString())) {
                //String returnType = new HandleReturnType().setTags(tags).setExcludeResponseObject(unboxExcludeList).setResponseObject(m.getReturnType().toString()).setText(cg.iterator().next().getSrc().method().getActiveBody().toString()).parser();
                String returnType = new HandleReturnType().setBody(cg.iterator().next().getSrc().method().getActiveBody()).setUnboxList(unboxList).setUnboxExcludeList(unboxExcludeList).parser(m.getReturnType().toString());
                if (returnType != null) {
                    if (returnType.contains("$")) {
                        returnType = returnType.replace("$", ".");
                    }
                    flowObject.setReturnType(returnType);
                }
            }

            //HandleEntityCrud1 handleEntityCrud = new HandleEntityCrud1().setCg(cg).build();
            //flowObject.setCrudToSubject(handleEntityCrud.getCrudToEntity());


            List<Integer> use = new ArrayList<>();
            buildTree(m,cg,flowObject,use);
            flowObject.setArgsname(getArgsName(m));
            logger.debug("build object flow for " + ((SootMethod) o).getName() );
        } catch (Exception e) {
           logger.error("fail build object flow for "+o.getClass().getTypeName());
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
                        //flowObject.setEntity(HandleEntity.isEntity(method.getDeclaringClass().getName()));
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
                logger.debug("successfully build CallGraph for " + sootMethod.getName());
            }
        } catch (Exception e) {
            logger.error("fail build method CallGraph for "+ sootMethod.getName());
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
                    //.setEntity(HandleEntity.isEntity(sootClass.getName()))
                    .build()
                    .getClassInfo();
        } catch (Exception e) {
           logger.error("fail geting class info ",e.getMessage());
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
            logger.debug("successfully got childes for " + sootMethod);
        } catch (Exception e) {
            logger.error("fail childes discovery for "+sootMethod,e.getMessage() );
        }
        return sootMethods;
    }
}
