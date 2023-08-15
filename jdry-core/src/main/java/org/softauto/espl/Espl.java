package org.softauto.espl;


import org.softauto.core.Multimap;
import org.softauto.core.Utils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.CompositeStringExpression;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Espl {

    StandardEvaluationContext  itemContext = new StandardEvaluationContext();
    //StandardEvaluationContext  runTimeItemContext = new StandardEvaluationContext();
    ExpressionParser parser = new SpelExpressionParser();
    private static Espl espl;
    Multimap publish;

    public static Espl getInstance(){
        if(espl == null){
            espl = new Espl();

        }
        return espl;
    }

    private Espl(){
        try {
            itemContext.setVariable("exec", EsplFunctions.class.getDeclaredMethod("exec",String.class));
            itemContext.setVariable("run", EsplFunctions.class.getDeclaredMethod("run",String.class,Object.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public Espl setPublish(Multimap publish) {
        this.publish = publish;
        return this;
    }



    public Object getProperty(String name){
        return itemContext.lookupVariable(name);
    }





    public static Espl reset(){
        return espl = new Espl();
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

    public Object parse(String expression){
        Object o = parser.parseExpression(expression);
        if(o instanceof String){
            return o.toString().replace("'","\"");
        }
        return o;
    }

    public Object  evaluate(String expression){
        Object o = null;
        if(expression.contains("#")) {
            if (expression.contains("${")) {
                o = parser.parseExpression(expression, new TemplateParserContextCompileTime()).getValue(itemContext);
            } else {
                o = parser.parseExpression(expression).getValue(itemContext);
            }
            if (o instanceof String) {
                return o.toString().replace("'", "\"");
            }
            return o;
        }
        return expression;
    }

    public Object  evaluate(String expression,Class type){
        Object o = null;
        if(expression.contains("#")) {
            if (expression.contains("${")) {
                o = parser.parseExpression(expression, new TemplateParserContextCompileTime()).getValue(itemContext,type);
            } else {
                o = parser.parseExpression(expression).getValue(itemContext,type);
            }
            if (o instanceof String) {
                return o.toString().replace("'", "\"");
            }
            return o;
        }
        return expression;
    }

}
