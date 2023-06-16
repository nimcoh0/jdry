package org.softauto.espl;


import org.softauto.core.Multimap;
import org.softauto.core.Utils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.CompositeStringExpression;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Espl {

    StandardEvaluationContext  compileTimeItemContext = new StandardEvaluationContext();
    StandardEvaluationContext  runTimeItemContext = new StandardEvaluationContext();
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
        initRunTime();
        initCompileTime();
    }

    public Espl setPublish(Multimap publish) {
        this.publish = publish;
        return this;
    }

    public Object getPropertyCompileTime(String name){
        return compileTimeItemContext.lookupVariable(name);
    }

    public Object getProperty(String name){
        return compileTimeItemContext.lookupVariable(name);
    }

    public Object getPropertyRunTime(String name){
        return runTimeItemContext.lookupVariable(name);
    }

    public void initRunTime(){
        try {
            //espl = this;
            EsplFunctionsRunTime.setPublish(publish);
            runTimeItemContext.setVariable("map", EsplFunctionsRunTime.Map.class.getDeclaredMethod("map", String.class));

            runTimeItemContext.setVariable("random", EsplFunctionsRunTime.Strategy.class.getDeclaredMethod("random", String.class));
            runTimeItemContext.setVariable("consume", EsplFunctionsRunTime.Consume.class.getDeclaredMethod("consume",String.class,String.class));
            //runTimeItemContext.setVariable("randomf", org.softauto.espl.EsplFunctions.Strategy.class.getDeclaredMethod("randomf",String.class));
            runTimeItemContext.setVariable("strategy", EsplFunctionsRunTime.Strategy.class.getDeclaredMethod("strategy",String.class));
            runTimeItemContext.setVariable("exclude", EsplFunctionsRunTime.Strategy.class.getDeclaredMethod("exclude",String.class));
            runTimeItemContext.setVariable("randomk", EsplFunctionsRunTime.Strategy.class.getDeclaredMethod("randomk",String.class,String[].class));

            //itemContext.setVariable("listener",Functions.class.getDeclaredMethod("listener", String.class));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void initCompileTime(){
        try {
            EsplFunctionsCompileTime.setPublish(publish);
            //compileTimeItemContext.setVariable("consume", EsplFunctions.Consume.class.getDeclaredMethod("consume",String.class,String.class));
            compileTimeItemContext.setVariable("getUri", EsplFunctionsCompileTime.HandleUri.class.getDeclaredMethod("getUri", String.class));
            compileTimeItemContext.setVariable("buildUri", EsplFunctionsCompileTime.HandleUri.class.getDeclaredMethod("buildUri", String.class,String.class,String[].class));
            //espl.addProperty("editUriAdvance", EsplFunctions.HandleUri.class.getDeclaredMethod("editUriAdvance", String.class,String.class,Integer.class));
            //espl.addProperty("buildUri", EsplFunctions.HandleUri.class.getDeclaredMethod("buildUri", String.class,String.class,List.class));
            //compileTimeItemContext.setVariable("step", EsplFunctions.Step.class.getDeclaredMethod("createStep", String.class,String.class));
            //compileTimeItemContext.setVariable("map", EsplFunctions.Map.setParameters(parameters).getDeclaredMethod("map",String.class));
            compileTimeItemContext.setVariable("random", EsplFunctionsCompileTime.Strategy.class.getDeclaredMethod("random",String.class));
            //compileTimeItemContext.setVariable("randomf", org.softauto.espl.EsplFunctions.Strategy.class.getDeclaredMethod("randomf",String.class));
            compileTimeItemContext.setVariable("strategy", EsplFunctionsCompileTime.Strategy.class.getDeclaredMethod("strategy",String.class));
            compileTimeItemContext.setVariable("exclude", EsplFunctionsCompileTime.Strategy.class.getDeclaredMethod("exclude",String.class));
            //compileTimeItemContext.setVariable("randomk", org.softauto.espl.EsplFunctions.Strategy.class.getDeclaredMethod("randomk",String.class,String[].class));

            //espl.addProperty("getUri", EsplFunctions.HandleUri.class.getDeclaredMethod("getUriWithParameter", String.class,String.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    public static Espl reset(){
        return espl = new Espl();
    }

    public Espl addProperty(String key, Object value){
        compileTimeItemContext.setVariable(key,value);
        runTimeItemContext.setVariable(key,value);
        return this;
    }

    public Espl addProperties(Map<String,Object> map){
        compileTimeItemContext.setVariables(map);
        runTimeItemContext.setVariables(map);
        return this;
    }

    public Espl addRootObject(Object o){
        compileTimeItemContext.setRootObject(o);
        runTimeItemContext.setRootObject(o);
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
        if(expression.contains("${")){
            return parser.parseExpression(expression, new TemplateParserContextCompileTime());
        }else if(expression.contains("@{")){
            return parser.parseExpression(expression, new TemplateParserContextRunTime());
        }else {
            return parser.parseExpression(expression);
        }
    }



    private Object _evaluate(String expression, StandardEvaluationContext  itemContext, ParserContext context,String symbole){
        String exp = expression =  Utils.javaEscape(expression);
        Object o =  parser.parseExpression(expression, context);
        if(o instanceof CompositeStringExpression){
            CompositeStringExpression compositeStringExpression = (CompositeStringExpression) o;

            for(Expression expression1 : compositeStringExpression.getExpressions()){
                if(expression1 instanceof SpelExpression){
                    String str  = parser.parseExpression(expression1.getExpressionString(), context).getValue(itemContext).toString();
                    if(str.contains("#")) {
                        itemContext.setVariable("addPlus",true);
                        itemContext.setVariable("expression",str);
                        exp = parser.parseExpression(str).getValue(itemContext).toString();
                        //itemContext.setVariable("expression",exp);
                    }
                    exp =   expression.replace(symbole+"{"+expression1.getExpressionString()+"}",exp);
                    expression = exp ;
                }
            }
        }else if(o instanceof SpelExpression){
            String str  = parser.parseExpression(((SpelExpression)o).getExpressionString(), context).getValue(itemContext).toString();
            if(str.contains("#")) {
                itemContext.setVariable("addPlus",false);
                itemContext.setVariable("expression",str);
                exp = parser.parseExpression(str).getValue(itemContext).toString();
                //itemContext.setVariable("expression",exp);
            }
            exp =   expression.replace(symbole+"{"+((SpelExpression)o).getExpressionString()+"}",exp);
            expression = exp ;
        }
        if(!exp.contains("#") && exp.contains("'")){
            exp =   exp.replace("'","\"");
        }
        return exp;
    }

    public Object  evaluate(String expression){
        //String exp = expression =  Utils.javaEscape(expression);
        try {
            //itemContext.setVariable("expression",Utils.javaEscape(exp));
            if(expression.contains("${")){
                return _evaluate(expression,compileTimeItemContext,new TemplateParserContextCompileTime(),"$");
            }else if(expression.contains("@{")) {
                return _evaluate(expression,runTimeItemContext,new TemplateParserContextRunTime(),"@");

            }else {
                compileTimeItemContext.setVariable("addPlus",false);
                compileTimeItemContext.setVariable("expression",expression);
                Object exp1 = parser.parseExpression(expression).getValue(compileTimeItemContext);
                return exp1;
            }
        }catch (Exception e){
            return expression;
        }
    }


    public Object evaluateToObject(String expression) {
        if(expression.contains("${")) {
           return evaluateToObject(expression,compileTimeItemContext,new TemplateParserContextCompileTime());
        }else if(expression.contains("@{")) {
            return evaluateToObject(expression,runTimeItemContext,new TemplateParserContextRunTime());
        }
        return this.evaluateToObject(expression,compileTimeItemContext,new TemplateParserContextCompileTime());

    }

    public Object  evaluateToObject(String expression,StandardEvaluationContext  itemContext, ParserContext context){
        Object exp = expression;
        try {
            if(expression.contains("${")) {
                CompositeStringExpression compositeStringExpression = (CompositeStringExpression) parser.parseExpression(expression, context);
                for (Expression expression1 : compositeStringExpression.getExpressions()) {
                    if (expression1 instanceof SpelExpression) {
                        String str = parser.parseExpression(expression1.getExpressionString(), context).getValue(itemContext).toString();
                        if (str.contains("#")) {
                            exp = parser.parseExpression(str).getValue(itemContext);
                        }


                    }
                }
            }else if(expression.contains("@{")){
                CompositeStringExpression compositeStringExpression = (CompositeStringExpression) parser.parseExpression(expression, context);
                for (Expression expression1 : compositeStringExpression.getExpressions()) {
                    if (expression1 instanceof SpelExpression) {
                        String str = parser.parseExpression(expression1.getExpressionString(), context).getValue(itemContext).toString();
                        if (str.contains("#")) {
                            exp = parser.parseExpression(str).getValue(itemContext);
                        }


                    }
                }
            }else {
                return parser.parseExpression(exp.toString()).getValue(itemContext);

            }

        }catch (Exception e){
            return exp;
        }

        return exp;
    }

}
