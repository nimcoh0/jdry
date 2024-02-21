package org.softauto.discovery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.Discover;
import org.softauto.analyzer.model.genericItem.External;
import org.softauto.config.Context;
import org.softauto.config.Configuration;
import org.softauto.core.Utils;
import org.softauto.filter.IFilter;
import org.softauto.handlers.HandleGenericDiscovery;
import org.softauto.handlers.HandleReturn;
import org.softauto.spel.SpEL;
import org.softauto.flow.*;
import org.softauto.state.ExternalFactory;
import soot.*;
import soot.jimple.toolkits.callgraph.CallGraph;


import java.util.*;

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
            SootMethod m = (SootMethod) o;
            SpEL.getInstance().addProperty("method",m);

            flowObject =  FlowBuilder.newBuilder().setStaticInitializer(m.isStaticInitializer()).setStatic(m.isStatic()).setConstructor(m.isConstructor()).setCg(cg).setName(m.getName()).setClazz(m.getDeclaringClass().getName()).setMethod(m).build().getFlowObject();
            List<String> unboxList = Configuration.has(Context.UNBOX_RETURN_TYPE) ? evaluateList(Configuration.get(Context.UNBOX_RETURN_TYPE).asList()): new ArrayList<>();
            List<String> unboxExcludeList = Configuration.has(Context.UNBOX_EXCLUDE_RETURN_TYPE) ? evaluateList(Configuration.get(Context.UNBOX_EXCLUDE_RETURN_TYPE).asList()): new ArrayList<>();
            String unboxReturnType = m.getReturnType().toString();
            LinkedList<String> responseChain = new LinkedList<>();
            HandleReturn handleReturn = null;

            UnitPatchingChain units = m.getActiveBody().getUnits();

            LinkedList<External> externals = new LinkedList<>();
            Body body = null;
                body = m.getActiveBody();;
                handleReturn = new HandleReturn().setBody(body).setUnboxList(unboxList).setUnboxExcludeList(unboxExcludeList);
                handleReturn.parser(unboxReturnType);
                unboxReturnType = handleReturn.getType();
                addResponseChain(handleReturn.getResponseChain(), responseChain);

            for(Unit unit : units){
                ExternalFactory externalFactory = new ExternalFactory().setSootClass(m.getDeclaringClass()).setSootMethod(m).setAllExternals(externals).setUnit(unit).setId1(externals.size()).setBody(body).setApiClass(cg.iterator().next().src().getDeclaringClass().getName()).setApiMethod(cg.iterator().next().src().getName()).setMethod(m.getName()).setClazz(m.getDeclaringClass().getName()).build();
                LinkedList<External> externals1 = externalFactory.getExternals();

                externals.addAll(externals1);
            }


                    flowObject.setExternals(externals);
                    if(unboxReturnType != null && !responseChain.contains(unboxReturnType) && !Utils.isPrimitive(unboxReturnType)){
                        responseChain.add(unboxReturnType);
                    }
                    String name = handleReturn != null ? handleReturn.getName() : null;
                    if(name != null  && name.startsWith("class ")){
                        name = name.substring(name.indexOf("L")+1,name.length() -2).replace("/",".");
                    }

                    flowObject.setResponseChain(responseChain);
                    if (name == null || name.contains("$stack")) {

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


            flowObject.setArgsname(getArgsName(m));
            HandleGenericDiscovery handleGenericDiscovery = new HandleGenericDiscovery().setFlowObject(flowObject).setTags(m.getTags()).build();
            if(handleGenericDiscovery.getGlobalGeneric() != null && handleGenericDiscovery.getGlobalGeneric().equals(unboxReturnType)){
                flowObject.setReturnTypeGeneric(true);
            }
            updateReturnTypeFromRequest(flowObject);
            logger.debug(JDRY,"build object flow for " + ((SootMethod) o).getName() );
        } catch (Exception e) {
           logger.error(JDRY,"fail build object flow for "+o.getClass().getTypeName());
        }
        return flowObject;
    }




    private void updateReturnTypeFromRequest(FlowObject flowObject ){
       for(int i=0;i<flowObject.getArgsname().size();i++){
            if(flowObject.getReturnTypeName() != null && flowObject.getReturnTypeName().equals(flowObject.getArgsname().get(i))){
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


}
