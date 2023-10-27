package org.softauto.discovery.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.SerializationUtils;
import soot.tagkit.ParamNamesTag;
import soot.tagkit.Tag;

import java.util.*;

public class HandleReturnType {

    private String text ;

    private String responseObject;

    private List<String> excludeResponseObject;

    private List<Integer> ignorLines = new ArrayList<>();

    private List<String> tags;

    private LinkedList<String> result = new LinkedList();

    public HandleReturnType setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public HandleReturnType setExcludeResponseObject(List<String> excludeResponseObject) {
        this.excludeResponseObject = excludeResponseObject;
        return this;
    }

    public HandleReturnType setResponseObject(String responseObject) {
        this.responseObject = responseObject;
        return this;
    }

    public HandleReturnType setText(String text) {
        this.text = text;
        return this;
    }


    public String parser() {
        String s = null;
        try {
            ignorLines = new ArrayList<>();
            String[] lines = text.split("\n");
           // String orgResponseObject = SerializationUtils.clone(responseObject);
            s = null;
            if(isTextContainResponseObjectInvoke(responseObject,lines)){
               // do {
                    s = find(responseObject, lines);
                   // if(s != orgResponseObject)
                    //    result.add(s);
               // }while (s != orgResponseObject);
            }
           // if(result.size()> 1){
              //  return new ObjectMapper().writeValueAsString(result);
          //  }else {
             //  return result.get(0);
          //  }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    private boolean isVarString(String var){
       if(var != null && !var.isEmpty() && var.charAt(0) == '\"'){
           return true;
       }
       return false;
    }

    private String getStuck(String s){

        if(s.contains("$stack") && s.contains(",")){
            String[] ss = s.split(",");
            for(String s1 : ss){
                if(s1.trim().startsWith("$")){
                    return s1.trim();
                }
            }
        }
        return s ;
    }

    private boolean isVar(String var){
        if(var.contains(",")){
            String[] list = var.trim().split(",");
            for(String s : list){
                if(s.startsWith("$")){
                    return true;
                }
            }
        }else {
            if(var.startsWith("$")){
                return true;
            }
        }
        return false;
    }

    private String parseLine(String line){
        if(line.contains("<"+responseObject)){

        }
        return null;
    }

    private String find(String responseObject,String[] lines){
        for(int i =1;i<lines.length;i++) {
            if (!ignorLines.contains(i)) {
                if (lines[i].contains( responseObject )){

                    String var = lines[i].contains("(") && lines[i].contains("<"+responseObject) ? lines[i].substring(lines[i].lastIndexOf("(") + 1, lines[i].lastIndexOf(")")): null;
                    if(var != null && excludeResponseObject != null && excludeResponseObject.contains(var)){
                        var = null;
                    }
                    if(var != null && var.equals(responseObject)){
                        var = lines[i].contains("(") ? lines[i].substring(lines[i].lastIndexOf(")") + 1, lines[i].length()): null;
                        if(var.endsWith(";")){
                            var = var.replace(";","");
                        }
                        //String type = getVarNameRealType(var, lines);
                        //result.add(type);
                        //return type;
                    }
                    ignorLines.add(i);
                    if (var != null && !var.isEmpty()) {
                        var = getStuck(var);
                        if (var.contains(",") ) {
                            return responseObject;
                        }
                            if (isVar(var)) {
                              //  var = getStuck(var);
                                String type = getVarNameRealType(var, lines);
                                if ( type != null && excludeResponseObject != null && !excludeResponseObject.contains(type) && excludeResponseObject.stream().noneMatch(str -> type.matches(str))) {
                                        responseObject = find(type, lines);
                                }
                            } else {
                                //if (!isParameter(var)) {
                                    String type = getVarNameRealType(var, lines);
                                    if (type != null) {
                                        if (type.startsWith("java") && excludeResponseObject != null && !excludeResponseObject.contains(type)) {
                                            return type;
                                        }else {
                                            if (type != null && excludeResponseObject != null && !excludeResponseObject.contains(type)) {
                                                responseObject = find(type, lines);

                                            }
                                        }
                                    }
                               // }else {
                               //     String type = getVarNameRealType(var, lines);
                               //     return type;
                               // }
                            }

                    }
                }
            }
        }

        return responseObject;
    }

    private String getType(String var,String[] lines){
        for(int i =1;i<lines.length;i++) {
            if (lines[i].contains( "this :=  ")){
                return null;
            }

            if (lines[i].contains( var )){
               String[] t = lines[i].trim().split(" ");
               return t[0];
            }
        }
       return null;
    }

    private boolean isParameter(String var){
        if(tags != null && tags.size() > 0) {
            for (String param : tags) {
                if (var.contains(param)) {
                    return true;
                }
            }
        }
        return false;
    }




    private boolean isTextContainResponseObjectInvoke(String responseObject,String[] lines){
        for(int i =0;i<lines.length;i++) {
            //if (lines[i].contains("<" + responseObject + ":")){
            if (lines[i].contains(responseObject)){
                return true;
            }
        }
        return false;
    }


    private String getVarNameRealType(String var,String[] lines){
        for(int i =0;i<lines.length;i++){
            if(lines[i].trim().startsWith("label")){
               // String result = var.replaceAll("^\"|\"$", "");
               // return result;
                return null;
            }
            if (lines[i].contains(var)) {
                String[] s = lines[i].trim().split(" ");
                return s[0].trim();
            }
        }
        return null;
    }

}
