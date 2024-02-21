package org.softauto.state;

import org.apache.commons.lang3.StringUtils;
import org.softauto.utils.Constants;
import soot.Type;
import soot.Unit;
import soot.Value;

import java.util.LinkedList;
import java.util.List;

public class ExternalHelper {

    public static LinkedList<String> setArgs(List<Value> args) {
        LinkedList<String> list = new LinkedList<>();
        for(Value value : args){
            list.add(value.toString());
        }
        return list;
    }

    public static LinkedList<String> setTypes(List<Type> types) {
        LinkedList<String> list = new LinkedList<>();
        for(Type type : types){
            list.add(type.toString());
        }
        return list;
    }



    public static String getMode(Unit unit){
        try{
            if(unit.getDefBoxes().size() > 0) {
                if (unit.getDefBoxes().get(0).getValue().getClass() == Class.forName("soot.jimple.StaticFieldRef") ||
                        unit.getDefBoxes().get(0).getValue().getClass() == Class.forName("soot.jimple.internal.JInstanceFieldRef")) {
                    return ("set");
                } else {
                    return ("get");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
