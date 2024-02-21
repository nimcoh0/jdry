package org.softauto.state;

import org.softauto.analyzer.model.genericItem.External;
import org.softauto.config.Configuration;
import org.softauto.config.Context;
import org.softauto.spel.SpEL;
import org.softauto.utils.Util;
import soot.*;
import soot.jimple.*;
import soot.jimple.internal.*;
import soot.tagkit.LineNumberTag;

import java.util.*;

public class ExternalVisitor {



    private String method;

    private String clazz;

    private boolean api = false;

    private  String apiClass ;

    private  String apiMethod ;

    private Unit unit;

    private static Map locals = new HashMap();

    public ExternalVisitor setApi(boolean api) {
        this.api = api;
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

    List<String> unboxList = Configuration.has(Context.UNBOX_RETURN_TYPE) ? evaluateList(Configuration.get(Context.UNBOX_RETURN_TYPE).asList()): new ArrayList<>();
    List<String> unboxExcludeList = Configuration.has(Context.UNBOX_EXCLUDE_RETURN_TYPE) ? evaluateList(Configuration.get(Context.UNBOX_EXCLUDE_RETURN_TYPE).asList()): new ArrayList<>();
    List<String> excludeMethodList = Configuration.has("exclude_method") ? evaluateList(Configuration.get("exclude_method").asList()): new ArrayList<>();
    List<String> excludeTypeList = Configuration.has("exclude_type") ? evaluateList(Configuration.get("exclude_type").asList()): new ArrayList<>();

    public ExternalVisitor setApiClass(String apiClass) {
        this.apiClass = apiClass;
        return this;
    }

    public ExternalVisitor setApiMethod(String apiMethod) {
        this.apiMethod = apiMethod;
        return this;
    }

    public ExternalVisitor setMethod(String method) {
        this.method = method;
        return this;
    }

    LinkedList<External> externals = new LinkedList<>();

    private static int id ;

    public boolean isApi() {
        return api;
    }


    public ExternalVisitor setClazz(String clazz) {
        this.clazz = clazz;
        return this;
    }


    public LinkedList<External> getExternals() {
        return externals;
    }

    public ExternalVisitor setId(int id) {
        this.id = id;
        return this;
    }

    public External visit(JInstanceFieldRef value,JimpleLocal local){
        return visit(value.getFieldRef(),local);
    }

    public External visit(StaticFieldRef value,JimpleLocal local){
        External external =  visit(value.getFieldRef(),local);
        for(External ext : externals){
            if(ext.getMode().equals("type") && ext.getNumber() == local.getNumber() ){
               external.setCrud(ext.getCrud());
            }
        }

        return external;
    }



    public External visit(RefType value,JimpleLocal local){
        External external = new External();
        if( !value.getSootClass().isEnum() && !value.getSootClass().isInterface() && !excludeTypeList.contains(value.toString()) && !excludeMethodList.contains(method) && !unboxList.contains(value.getSootClass().getName()) && !unboxList.contains(clazz) && !unboxExcludeList.contains(value.getSootClass().getName()) && !unboxExcludeList.contains(clazz) && !clazz.contains("$") && !value.getSootClass().getName().contains("$")  && !Util.isEntity(value.getSootClass())) {
            external.setId(id++);
            external.setApiClass(apiClass);
            external.setApiMethod(apiMethod);
            external.setTarget(value.getSootClass().getName());
            external.setClazz(clazz);
            external.setMethod(method);
            external.setCrud(ExternalHelper.getMode(unit));
            external.setCallMethod(method);
            external.setType(value.toString());
            external.setStatic(false);
            external.setMode("type");
            external.setNumber(local.getNumber());
            String defbox = "";
            if (unit.getDefBoxes().size() > 0) {
                defbox = unit.getDefBoxes().get(0).getValue().getClass().getTypeName();
            }
            String useBox = "";
            if (unit.getUseBoxes().size() > 0) {
                useBox = unit.getUseBoxes().get(0).getValue().getClass().getTypeName();
            }
            external.setSootType(unit.getClass().getTypeName() + " " + defbox + " " + useBox);
            if (!Util.isExist(externals, external)) {
                externals.add(external);
            }
        }
        return external;
    }

    public External visit(SootFieldRef value,JimpleLocal local){
        External external = new External();
        if(!excludeTypeList.contains(value.type().toString()) && !excludeMethodList.contains(method) && !unboxList.contains(value.resolve().getDeclaringClass().getName()) && !unboxList.contains(clazz) && !unboxExcludeList.contains(value.resolve().getDeclaringClass().getName()) && !unboxExcludeList.contains(clazz) && !clazz.contains("$") && !value.resolve().getDeclaringClass().getName().contains("$") && value.resolve().getDeclaringClass().getName().contains(Configuration.get(Context.DOMAIN).asString()) && !Util.isEntity(value.resolve().getDeclaringClass())) {
            if (!Util.isEntity(value.resolve().getDeclaringClass()) && !value.resolve().getDeclaringClass().isEnum() && !value.resolve().getDeclaringClass().isInterface() ) {
                external.setId(id++);
                external.setApiClass(apiClass);
                external.setApiMethod(apiMethod);
                external.setName(value.name());
                external.setTarget(value.resolve().getDeclaringClass().getName());
                external.setClazz(value.declaringClass().getName());
                external.setMethod(method);
                external.setCrud(ExternalHelper.getMode(unit));
                external.setCallMethod(method);
                external.setType(value.type().toString());
                external.setStatic(value.isStatic());
                external.setMode("field");
                external.setNumber(local.getNumber());
                String defbox = "";
                if (unit.getDefBoxes().size() > 0) {
                    defbox = unit.getDefBoxes().get(0).getValue().getClass().getTypeName();
                }
                String useBox = "";
                if (unit.getUseBoxes().size() > 0) {
                    useBox = unit.getUseBoxes().get(0).getValue().getClass().getTypeName();
                }
                external.setSootType(unit.getClass().getTypeName() + " " + defbox + " " + useBox);
                if (!Util.isExist(externals, external)) {
                    externals.add(external);
                }
            }
        }
        return external;
    }

    public void visit(JInvokeStmt value){
        unit = value;
        if(value.getInvokeExpr() instanceof JSpecialInvokeExpr){
            visit((JSpecialInvokeExpr) value.getInvokeExpr(),(JimpleLocal)((JSpecialInvokeExpr) value.getInvokeExpr()).getBaseBox().getValue());
        }
        if(value.getInvokeExpr() instanceof JVirtualInvokeExpr) {
            visit((JVirtualInvokeExpr) value.getInvokeExpr(),(JimpleLocal)((JVirtualInvokeExpr) value.getInvokeExpr()).getBaseBox().getValue());
        }
        if(value.getInvokeExpr() instanceof JStaticInvokeExpr) {
            visit((JStaticInvokeExpr) value.getInvokeExpr(),null);
        }
        if(value.getInvokeExpr() instanceof JInterfaceInvokeExpr) {
            visit((JInterfaceInvokeExpr) value.getInvokeExpr(),(JimpleLocal)((JInterfaceInvokeExpr) value.getInvokeExpr()).getBaseBox().getValue());
        }
        if(value.getInvokeExpr() instanceof JimpleLocal) {
           return;
        }
    }

    public External visit(JVirtualInvokeExpr value,JimpleLocal local){
        return visit(value.getMethodRef(),value.getArgs(),local);
    }

    public External visit(JSpecialInvokeExpr value,JimpleLocal local){
        return visit(value.getMethodRef(),value.getArgs(),local);
    }



    public  External visit(JStaticInvokeExpr value,JimpleLocal local){
        return visit(value.getMethodRef(),value.getArgs(),local);
    }

    public  External visit(JInterfaceInvokeExpr value,JimpleLocal local){
       return visit(value.getMethodRef(),value.getArgs(),local);
    }

    public External visit(SootMethodRef value, List<Value> args,JimpleLocal local){
        External external = new External();
        if(!value.resolve().getDeclaringClass().isEnum() && !value.resolve().getDeclaringClass().isInterface() && !excludeMethodList.contains(value.getName()) && !unboxList.contains(value.resolve().getDeclaringClass().getName()) && !unboxList.contains(clazz) && !unboxExcludeList.contains(value.resolve().getDeclaringClass().getName()) && !unboxExcludeList.contains(clazz) && !clazz.contains("$") && !value.resolve().getDeclaringClass().getName().contains("$") && value.resolve().getDeclaringClass().getName().contains(Configuration.get(Context.DOMAIN).asString())&& !Util.isEntity(value.resolve().getDeclaringClass())) {
            try {
                external.setId(id++);
                external.setApiClass(apiClass);
                external.setApiMethod(apiMethod);
                external.setTarget(value.resolve().getDeclaringClass().getName());
                external.setCallMethod(method);
                external.setApi(isApi());
                external.setClazz(clazz);
                external.setMethod(value.getName());
                external.setResult(value.getReturnType().toString());
                external.setTypes(ExternalHelper.setTypes(value.getParameterTypes()));
                external.setArgs(ExternalHelper.setArgs(args));
                external.setMode("method");
                String defbox = "";
                if (unit.getDefBoxes().size() > 0) {
                    defbox = unit.getDefBoxes().get(0).getValue().getClass().getTypeName();
                }
                String useBox = "";
                if (unit.getUseBoxes().size() > 0) {
                    useBox = unit.getUseBoxes().get(0).getValue().getClass().getTypeName();
                }
                external.setSootType(unit.getClass().getTypeName() + " " + defbox + " " + useBox);
                external.setStatic(value.isStatic());
                external.setNumber(local != null ? local.getNumber() : -1);
                if (!Util.isExist(externals, external)) {
                    externals.add(external);
                }
                external.setCrud(ExternalHelper.getMode(unit));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return external;
    }


    public External visit(JNewExpr value,JimpleLocal local){
        External external =  visit(value.getBaseType(),local);
        external.setCrud("new");
        return external;
    }

    public void visit(JAssignStmt value){
        unit = value;

        if(value.getLeftOpBox().getValue() instanceof JVirtualInvokeExpr && value.getRightOpBox().getValue() instanceof JimpleLocal) {
            visit((JVirtualInvokeExpr)value.getRightOpBox().getValue(),(JimpleLocal)value.getRightOpBox().getValue());
        }
        if(value.getLeftOpBox().getValue() instanceof JSpecialInvokeExpr && value.getRightOpBox().getValue() instanceof JimpleLocal) {
            visit((JSpecialInvokeExpr)value.getRightOpBox().getValue(),(JimpleLocal)value.getRightOpBox().getValue());
        }
        if(value.getLeftOpBox().getValue() instanceof JStaticInvokeExpr && value.getRightOpBox().getValue() instanceof JimpleLocal) {
            visit((JStaticInvokeExpr)value.getRightOpBox().getValue(),(JimpleLocal)value.getRightOpBox().getValue());
        }
        if(value.getLeftOpBox().getValue() instanceof JInterfaceInvokeExpr && value.getRightOpBox().getValue() instanceof JimpleLocal) {
            visit((JInterfaceInvokeExpr)value.getRightOpBox().getValue(),(JimpleLocal)value.getRightOpBox().getValue());
        }
        if(value.getLeftOpBox() instanceof JAssignStmt.LinkedVariableBox) {

        }
        if(value.getLeftOpBox().getValue() instanceof StaticFieldRef && value.getRightOpBox().getValue() instanceof JimpleLocal) {
            visit((StaticFieldRef)value.getLeftOpBox().getValue(),(JimpleLocal)value.getRightOpBox().getValue());
        }
        if(value.getLeftOpBox() instanceof JAssignStmt.LinkedRValueBox) {

        }
        if(value.getLeftOpBox().getValue() instanceof JStaticInvokeExpr && value.getRightOpBox().getValue() instanceof JimpleLocal) {
            visit((JStaticInvokeExpr)value.getLeftOpBox().getValue(),(JimpleLocal)value.getRightOpBox().getValue());
        }
        if(value.getLeftOpBox().getValue() instanceof JInstanceFieldRef && value.getRightOpBox().getValue() instanceof JimpleLocal) {
            visit((JInstanceFieldRef)value.getLeftOpBox().getValue(),(JimpleLocal)value.getRightOpBox().getValue());
        }
        if(value.getRightOpBox() instanceof JAssignStmt.LinkedVariableBox) {

        }
        if(value.getRightOpBox().getValue() instanceof JStaticInvokeExpr) {

        }
        if(value.getRightOpBox().getValue() instanceof StaticFieldRef && value.getLeftOpBox().getValue() instanceof JimpleLocal) {
            visit((StaticFieldRef)value.getRightOpBox().getValue(),(JimpleLocal)value.getLeftOpBox().getValue());
        }

        if(value.getRightOpBox().getValue() instanceof JNewExpr && value.getLeftOpBox().getValue() instanceof JimpleLocal) {
            visit((JNewExpr)value.getRightOpBox().getValue(),(JimpleLocal)value.getLeftOpBox().getValue());
        }
        if(value.getRightOpBox().getValue() instanceof JStaticInvokeExpr && value.getLeftOpBox().getValue() instanceof JimpleLocal) {
            visit((JStaticInvokeExpr)value.getRightOpBox().getValue(),(JimpleLocal)value.getLeftOpBox().getValue());
        }
        if(value.getRightOpBox().getValue() instanceof JInstanceFieldRef && value.getLeftOpBox().getValue() instanceof JimpleLocal) {
            visit((JInstanceFieldRef)value.getRightOpBox().getValue(),(JimpleLocal)value.getLeftOpBox().getValue());
        }
        if(value.getRightOpBox().getValue() instanceof JVirtualInvokeExpr && value.getLeftOpBox().getValue() instanceof JimpleLocal) {
            visit((JVirtualInvokeExpr)value.getRightOpBox().getValue(),(JimpleLocal)value.getLeftOpBox().getValue());
        }
        if(value.getRightOpBox().getValue() instanceof JSpecialInvokeExpr && value.getLeftOpBox().getValue() instanceof JimpleLocal) {
            visit((JSpecialInvokeExpr)value.getRightOpBox().getValue(),(JimpleLocal)value.getLeftOpBox().getValue());
        }

        if(value.getRightOpBox().getValue() instanceof JInterfaceInvokeExpr && value.getLeftOpBox().getValue() instanceof JimpleLocal) {
            visit((JInterfaceInvokeExpr)value.getRightOpBox().getValue(),(JimpleLocal)value.getLeftOpBox().getValue());
        }
        if(value.getRightOpBox().getValue() instanceof JimpleLocal && value.getLeftOpBox().getValue() instanceof JimpleLocal) {

        }
    }






}
