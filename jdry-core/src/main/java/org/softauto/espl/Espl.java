package org.softauto.espl;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.core.Multimap;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Espl {


    private static Logger logger = LogManager.getLogger(Espl.class);


    StandardEvaluationContext  itemContext = new StandardEvaluationContext();
    //StandardEvaluationContext  runTimeItemContext = new StandardEvaluationContext();
    ExpressionParser parser = new SpelExpressionParser();
    private static Espl espl;
    Multimap publish;

    ClassLoader classLoader;

    public Espl setClassLoader(ClassLoader classLoader) {
        try {
            this.classLoader = classLoader;
            //StandardTypeLocator standardTypeLocator = new StandardTypeLocator(classLoader);
            //standardTypeLocator.registerImport("org.traccar");
            //Class<?> c = standardTypeLocator.findType("org.traccar.TestLib");
            //Class c = ClassUtils.getClass("org.traccar.TestLib");
            //itemContext.setTypeLocator(standardTypeLocator);
            //itemContext.setVariable("TestLib",c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

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
            itemContext.setVariable("step", EsplFunctions.class.getDeclaredMethod("step",String.class));
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
        try {
            if(expression != null && expression.contains("#")) {
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
        } catch (Exception e) {
            logger.error(e.getMessage());
            return expression;
        }

        return expression;
    }

}
