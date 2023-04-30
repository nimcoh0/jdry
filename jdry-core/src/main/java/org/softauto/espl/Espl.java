package org.softauto.espl;


import org.softauto.core.Multimap;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Espl {

    StandardEvaluationContext  itemContext = new StandardEvaluationContext();
    ExpressionParser parser = new SpelExpressionParser();

    Multimap publish;

    public Espl setPublish(Multimap publish) {
        this.publish = publish;
        return this;
    }

    public Espl(){
        try {
            EsplFunctions.setPublish(publish);
            itemContext.setVariable("map", EsplFunctions.Map.class.getDeclaredMethod("map", String.class));
            //itemContext.setVariable("listener",Functions.class.getDeclaredMethod("listener", String.class));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public Espl addProperty(String key, Object value){
        itemContext.setVariable(key,value);
        return this;
    }

    public Espl addProperties(Map<String,Object> map){
        itemContext.setVariables(map);
        return this;
    }

    public Espl addRootObject(Object o){
        itemContext.setRootObject(o);
        return this;
    }

    private String getVariable(String exp,String tag){
        Matcher matcher = Pattern.compile(tag+"\\s*(\\w+)").matcher(exp);
        while (matcher.find()) {
            return matcher.group(1);
        }
        return exp;
    }

    public Object  evaluate(String expression){
       return evaluate(expression,itemContext);
    }

    public Object  evaluate(String expression,StandardEvaluationContext  itemContext){
        String exp = expression;
        try {
            if(expression.contains("${")){
                exp  = parser.parseExpression(expression, new TemplateParserContext()).getValue(itemContext).toString();
            }else {
                exp = parser.parseExpression(exp).getValue(itemContext).toString();
            }
               // BracketsUtils bracketsUtils = new BracketsUtils().setExpression(expression).setOpenBracketTag("${").setCloseBracketTag("}").analyze();
               // for(Map.Entry entry : bracketsUtils.getGroups().entrySet()){
               //     String block = (String) expression.toString().subSequence(Integer.valueOf(entry.getKey().toString()),Integer.valueOf(entry.getValue().toString()));
               //     String var = getVariable(block,"#");
               //     if(itemContext.lookupVariable(var) != null) {

                       // exp = exp.replace(block, s);
         //           }
         //       }
           // }
            /*
            if(exp.contains("#")) {
                String var = getVariable(exp,"#");
                if(itemContext.lookupVariable(var) != null) {
                    exp = parser.parseExpression(exp).getValue(itemContext).toString();
                }
            }

             */
        }catch (Exception e){
            return exp;
        }
        return exp;
    }



}
