package org.softauto.core;

import org.apache.commons.lang3.text.StrSubstitutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Resolver {

    private static Resolver INSTANCE;
    Object exp;
    ConcurrentHashMap<String, Object> variables = new ConcurrentHashMap<>();
    String _key;

    private Resolver(){

    }

    public static Resolver getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Resolver();
        }

        return INSTANCE;
    }

    public  Resolver addVariables(ConcurrentHashMap<String, Object> variables){
        this.variables.putAll(variables);
        return this;
    }

    public  boolean addVariable(String key,Object value){
        //if(value.contains("${")) {
          //  return false;
        //}
        if(key != null) {
            if (key.contains("${")) {
                Pattern pattern = Pattern.compile("\\$\\{([^\\}]*)\\}");
                key = pattern
                        .matcher(key)
                        .results()
                        .collect(Collectors.toList())
                        .get(0)
                        .group(1);
            }
            _key = key;
            variables.put(key, value);
            return true;
        }
        return false;
    }


    public  String getKey(){
        return _key;
    }

    public  Resolver clean(){
        variables = new ConcurrentHashMap<>();
        return this;
    }

    public  Resolver setExpression(Object expression){
        exp = expression;
        return this;
    }

    public Object resolve(){
        if(exp != null) {
            if (exp instanceof ArrayList) {
                List<String> exps = new ArrayList<>();
                ((ArrayList<?>) exp).forEach((k) -> {
                    exps.add(StrSubstitutor.replace(k, variables));
                });
                return exps;
            }
            if (exp instanceof Map) {
                Map<String, Object> exps = new HashMap<>();
                ((Map<?, ?>) exp).forEach((k, v) -> {
                    exps.put(StrSubstitutor.replace(k, variables), v);
                });
                return exps;
            } else {
                if (exp.toString().contains("${")) {
                    return StrSubstitutor.replace(exp, variables);
                } else if (variables.size() > 0 && variables.keySet().contains(exp)) {
                    return variables.get(exp);
                }
            }
        }
        return exp;
    }

}
