package org.softauto.signature;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class DescToNativeSignature {

    private String sig;

    private ReturnType returnType = new ReturnType();

    private Generic genericType = new Generic();

    private None none = new None();

    private String name;

    private String subSignature;

    private ParametersType parametersType = new ParametersType();

    public String getGenericType() {
        return genericType.getResult();
    }

    public String getReturnType() {
        return returnType.getResult();
    }

    public List<String> getParametersType() {
        List<String> r = new ArrayList<>();
        LinkedList<HashMap<String,Boolean>> l = parametersType.getResult();
        for(HashMap<String,Boolean> m : l){
            for(Map.Entry entry : m.entrySet()){
                r.add(entry.getKey().toString());
            }
        }
        return r;
    }

    public DescToNativeSignature setName(String name) {
        this.name = name;
        return this;
    }

    private String cleanVar(String name){
        return name.substring(1,name.length()-1);
    }

    public String getSubSignature() {
        List<String> p = new ArrayList<>();
        for(String s : getParametersType()){
            p.add(cleanVar(s));
        }
        if(p.isEmpty()){
            subSignature = cleanVar(returnType.getResult())+ " " + name + "()";
        }else {
            subSignature = cleanVar(returnType.getResult()) + " " + name + "(" + StringUtils.join(p, ",") + ")";
        }
        return subSignature;
    }

    public DescToNativeSignature setSig(String sig) {
        this.sig = sig;
        return this;
    }

    public DescToNativeSignature parse(){
        LinkedList<String> stuck = new LinkedList<>();
        String var = new String();
        //boolean parameterized  = false;
        Types type = Types.NONE;
        ResultInterface result = null;
        char[] chars = sig.toCharArray();
        for(Character character : chars){
            try {
                switch (character.toString()) {
                    case "(":
                        result = parametersType;
                        break;
                    case ")":
                        result = returnType;
                        type = Types.NONE;
                        break;
                    case "L":
                        if(result == null){
                            result = returnType;
                        }
                        if (type.equals(Types.NONE)) {
                            type = Types.CLASS;
                            var = new String();
                            var = var + character;
                        } else {
                            var = var + character;
                        }
                        break;
                    case "T":
                       if (type.equals(Types.NONE)) {
                           var = new String();
                           var = var + character;
                           type = Types.GENERIC;
                           //result = genericType;
                       } else {
                           var = var + character;
                       }
                      break;
                    case "<":
                        var = var + character;
                        if (!type.equals(Types.NONE)) {
                            // parameterized = true;
                            stuck.push(var);
                            var = new String();
                        }else {
                            var = new String();
                            //var = var + character;
                            type = Types.GENERIC;
                            result = genericType;
                        }
                        break;
                    case ">":
                        var = var + character;
                        // if(!type.equals(Types.NONE)) {
                        //parameterized = !parameterized;
                        if (!stuck.isEmpty())
                            var = stuck.pop() + var;
                        //}
                        // result.apply(new VarType().setVar(var).apply(type.getValue()));
                        break;
                    case "V":
                        if(result == null){
                            result = returnType;
                        }
                        if (type.equals(Types.NONE)) {
                            type = Types.VOID;
                            result.apply("V");
                            type = Types.NONE;
                        } else {
                            var = var + character;
                        }
                        break;
                    case "I":
                        if(result == null){
                            result = returnType;
                        }
                        if (type.equals(Types.NONE)) {
                            type = Types.INT;
                            result.apply("I");
                            type = Types.NONE;
                        } else {
                            var = var + character;
                        }
                        break;
                    case "B":
                        if(result == null){
                            result = returnType;
                        }
                        if (type.equals(Types.NONE)) {
                            type = Types.BYTE;
                            result.apply("B");
                            type = Types.NONE;
                        } else {
                            var = var + character;
                        }
                        break;
                    case "C":
                        if(result == null){
                            result = returnType;
                        }
                        if (type.equals(Types.NONE)) {
                            type = Types.CHAR;
                            result.apply("C");
                            type = Types.NONE;
                        } else {
                            var = var + character;
                        }
                        break;
                    case "D":
                        if(result == null){
                            result = returnType;
                        }
                        if (type.equals(Types.NONE)) {
                            type = Types.DOUBLE;
                            result.apply("D");
                            type = Types.NONE;
                        } else {
                            var = var + character;
                        }
                        break;
                    case "F":
                        if(result == null){
                            result = returnType;
                        }
                        if (type.equals(Types.NONE)) {
                            type = Types.FLOAT;
                            result.apply("F");
                            type = Types.NONE;
                        } else {
                            var = var + character;
                        }
                        break;
                    case "J":
                        if(result == null){
                            result = returnType;
                        }
                        if (type.equals(Types.NONE)) {
                            type = Types.LONG;
                            result.apply("J");
                            type = Types.NONE;
                        } else {
                            var = var + character;
                        }
                        break;
                    case "S":
                        if(result == null){
                            result = returnType;
                        }
                        if (type.equals(Types.NONE)) {
                            type = Types.SHORT;
                            result.apply("S");
                            type = Types.NONE;
                        } else {
                            var = var + character;
                        }
                        break;
                    case "Z":
                        if(result == null){
                            result = returnType;
                        }
                        if (type.equals(Types.NONE)) {
                            type = Types.BOOLEAN;
                            result.apply("Z");
                            type = Types.NONE;
                        } else {
                            var = var + character;
                        }
                        break;
                    case "[":
                        if (type.equals(Types.NONE)) {
                            type = Types.ARRAY;
                            var = new String();
                            var = var + character;
                            //result.apply(new VarType().setVar(var).apply(type.getValue()));
                            //type = Types.NONE;
                        } else {
                            var = var + character;
                        }
                        break;
                    case ";":
                        var = var + character;
                        if (stuck.isEmpty()) {
                            if(result == null){
                                result = returnType;
                            }
                            result.apply(new VarType().setVar(var).apply(type.getValue()));
                            type = Types.NONE;
                            //result = none;
                        } else {
                            // var = var + character;
                        }
                        break;

                    default:
                        var = var + character;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //returnType = result.getResult().toString();
        return this;
    }
}
