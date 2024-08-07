package org.softauto.signature;

import java.util.HashMap;
import java.util.LinkedList;

public class SignatureParser {

    private String sig;

    private ReturnType returnType = new ReturnType();

    private Generic genericType = new Generic();

    private None none = new None();

    private ParametersType parametersType = new ParametersType();

    public String getGenericType() {
        return genericType.getResult();
    }

    public String getReturnType() {
        return returnType.getResult();
    }

    public LinkedList<HashMap<String,Boolean>> getParametersType() {
        return parametersType.getResult();
    }

    public SignatureParser setSig(String sig) {
        this.sig = sig;
        return this;
    }

    public SignatureParser parse(){
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
                   // case "T":
                   //    if (type.equals(Types.NONE)) {
                    //        var = new String();
                    //        var = var + character;
                    //        type = Types.GENERIC;
                   //         result = genericType;
                    //    } else {
                    //        var = var + character;
                    //    }
                    //    break;
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
                            result.apply(new VarType().setVar("V").apply(type.getValue()));
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
                            result.apply(new VarType().setVar("I").apply(type.getValue()));
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
                            result.apply(new VarType().setVar("B").apply(type.getValue()));
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
                            result.apply(new VarType().setVar("C").apply(type.getValue()));
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
                            result.apply(new VarType().setVar("D").apply(type.getValue()));
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
                            result.apply(new VarType().setVar("F").apply(type.getValue()));
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
                            result.apply(new VarType().setVar("J").apply(type.getValue()));
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
                            result.apply(new VarType().setVar("S").apply(type.getValue()));
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
                            result.apply(new VarType().setVar("Z").apply(type.getValue()));
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
