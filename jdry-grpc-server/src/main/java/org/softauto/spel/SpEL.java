package org.softauto.spel;


import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.core.Multimap;
import org.softauto.listener.impl.ExternalListener;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpEL {

    static org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ExternalListener.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    StandardEvaluationContext  itemContext = new StandardEvaluationContext();
    ExpressionParser parser = new SpelExpressionParser();
    private static SpEL spEL;
    Multimap publish;

    public static SpEL getInstance(){
        if(spEL == null){
            spEL = new SpEL();

        }
        return spEL;
    }


    public SpEL setPublish(Multimap publish) {
        this.publish = publish;
        return this;
    }



    public Object getProperty(String name){
        return itemContext.lookupVariable(name);
    }





    public static SpEL reset(){
        return spEL = new SpEL();
    }

    public SpEL addProperty(String key, Object value){
        itemContext.setVariable(key,value);
        return this;
    }

    public SpEL addProperties(Map<String,Object> map){
        itemContext.setVariables(map);
        return this;
    }

    public SpEL addRootObject(Object o){
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
        try {
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
        } catch (Throwable e) {
            logger.warn(JDRY,"fail evaluate "+ expression,e);
            return expression;
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
