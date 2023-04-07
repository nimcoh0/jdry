package org.softauto.espl1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.softauto.core.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Evaluate {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Evaluate.class);


    public Object invoke(ExpressionParser expressionParser, List<Object> context) {
        String result = null;
        try {
            String var =  invoke(expressionParser.getExp(),context).toString();
            String before = expressionParser.getBefore();
            String after = expressionParser.getAfter();
            if(before != null && !before.isEmpty()){
                result =before;
            }
            result = result + var;
            if(after != null && !after.isEmpty()){
                result =  result + after;
            }
        } catch (Exception e) {
            logger.error("fail evaluate "+ expressionParser.getExp(), e.getMessage());
        }
        return result;
    }


    public Object invoke(Object expression, List<Object> context) {
        try {
            String str ;
            if(expression instanceof String) {
                str = expression.toString();
                if (str.contains(".")) {
                    try {
                        ExpressionBuilder expressionBuilder = new ExpressionBuilder().newExpression(str).build();
                        EvulExp evulExp = new EvulExp();
                        Object o = null;
                        if (context != null) {
                            buildContext(context, evulExp, null);
                            //evulExp.addContexts("Device",context.get(0));
                            o = evulExp.setExp(expressionBuilder).evaluate();
                        } else {
                            o = evulExp.evulExp(str);
                        }

                        return o;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return str;
                    }
                } else {
                    return str;
                }
            }else if(expression instanceof ArrayList) {
                return expression;
            }else if(expression instanceof Map) {
                String exp =null;
                Object ctx =null;
                if(((Map<?, ?>) expression).containsKey("expression")){
                    exp = ((Map<?, ?>) expression).get("expression").toString();
                }
                if(((Map<?, ?>) expression).containsKey("context")){
                    ctx = ((Map<?, ?>) expression).get("context");
                }
                if(exp != null && ctx != null){
                    //ExpressionBuilder expressionBuilder = new ExpressionBuilder().newExpression(ctx.getClass().getSimpleName()+exp).build();
                    EvulExp evulExp = new EvulExp();
                    buildContext(ctx, evulExp, null);
                    return evulExp.evulExp(exp,ctx);
                }else {
                    return expression;
                }
            }
        } catch (Exception e) {
            logger.error("fail evaluate "+ expression, e.getMessage());
        }
        return expression;
    }

    private Object buildContext(Object context,EvulExp evulExp,String name){
        try {
            if(context != null) {
                if (context instanceof ArrayList) {
                    for (Object o : (ArrayList) context) {
                        buildContext(o, evulExp, name);
                    }
                } else if (context instanceof Map) {
                    for (Map.Entry entry : ((Map<?, ?>) context).entrySet()) {
                        name = entry.getKey().toString();
                        Object o1 = buildContext(entry.getValue(), evulExp, name);
                    }
                } else if (Utils.isJson(context.toString())) {
                    if (context != null && name != null)
                        evulExp.addContexts(name, StringToObject(context));
                } else if (context.getClass().isPrimitive()) {


                } else if (context instanceof String) {
                    if (context != null && name != null)
                        evulExp.addContexts(name, StringToObject(context));
                }else {
                    evulExp.addContexts(context.getClass().getSimpleName(), context);
                       /*
                        Field[] fields = context.getClass().getDeclaredFields();
                        try {
                            for (Field f : fields) {
                                name = f.getName();
                                Object value = FieldUtils.readField(context, f.getName(), true);
                                if (value instanceof ArrayList) {
                                    for (Object o : (ArrayList) value) {
                                        buildContext(o, evulExp,name);
                                    }
                                }else if(value instanceof Map) {
                                    for (Map.Entry entry : ((HashMap<?, ?>) value).entrySet()) {
                                        buildContext(entry.getValue(), evulExp,name);
                                    }
                                }
                                else {
                                    if (value != null && name != null)
                                        evulExp.addContexts(name, StringToObject(value));
                                }
                            }
                        } catch (IllegalAccessException e) {
                            logger.error("fail build Context ", e.getMessage());
                        }

                        */
                }


            }


        } catch (SecurityException e) {
            logger.error("fail build Context ", e.getMessage());
        }


        return context;
    }

    private static Object StringToObject(Object str){
        try {
            String s = null;
            if(!Utils.isJson(str.toString())){
                s = new ObjectMapper().writeValueAsString(str);
            }else {
                s = str.toString();
            }
            return  new ObjectMapper().readTree(s);
        } catch (JsonProcessingException e) {
            logger.error("fail convert String to Object for  "+ str, e.getMessage());
        }
        return null;
    }


}
