package org.softauto.espl;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.softauto.core.Multimap;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.CompositeStringExpression;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Espl {

    StandardEvaluationContext  itemContext = new StandardEvaluationContext();
    ExpressionParser parser = new SpelExpressionParser();
    private static Espl espl;
    Multimap publish;

    public static Espl getInstance(){
        if(espl == null){
            espl = new Espl();
        }
        return espl;
    }

    public Espl setPublish(Multimap publish) {
        this.publish = publish;
        return this;
    }

    public Object getProperty(String name){
        return itemContext.lookupVariable(name);
    }

    private Espl(){
        try {
            //espl = this;
            EsplFunctions.setPublish(publish);
            itemContext.setVariable("map", EsplFunctions.Map.class.getDeclaredMethod("map", String.class));
            //itemContext.setVariable("listener",Functions.class.getDeclaredMethod("listener", String.class));
        }catch (Exception e){
            e.printStackTrace();
        }
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
        if(expression.contains("${")){
            return parser.parseExpression(expression, new TemplateParserContext());
        }else {
            return parser.parseExpression(expression);
        }
    }

    public Object  evaluate(String expression){
       return evaluate(expression,itemContext);
    }

    public Object  evaluate(String expression,StandardEvaluationContext  itemContext){
        String exp = expression;
        try {
            if(expression.contains("${")){
               Object o =  parser.parseExpression(expression, new TemplateParserContext());
               if(o instanceof CompositeStringExpression){
               CompositeStringExpression compositeStringExpression = (CompositeStringExpression) o;
               for(Expression expression1 : compositeStringExpression.getExpressions()){
                    if(expression1 instanceof SpelExpression){
                      String str  = parser.parseExpression(expression1.getExpressionString(), new TemplateParserContext()).getValue(itemContext).toString();
                      if(str.contains("#")) {
                         exp = parser.parseExpression(str).getValue(itemContext).toString();
                      }
                      exp =   expression.replace("${"+expression1.getExpressionString()+"}",exp);

                    }
                }
               }else if(o instanceof SpelExpression){
                   String str  = parser.parseExpression(((SpelExpression)o).getExpressionString(), new TemplateParserContext()).getValue(itemContext).toString();
                   if(str.contains("#")) {
                       exp = parser.parseExpression(str).getValue(itemContext).toString();
                   }
                   exp =   expression.replace("${"+((SpelExpression)o).getExpressionString()+"}",exp);

               }
            }else {
                Object exp1 = parser.parseExpression(exp).getValue(itemContext);
                if(exp1 instanceof String){
                    exp = exp1.toString();
                }else {
                   exp =  new ObjectMapper().writeValueAsString(exp1);
                }
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
        if(!exp.contains("#") && exp.contains("'")){
          exp =   exp.replace("'","\"");
        }
        return exp;
    }


    public Object evaluateToObject(String expression) {
        return this.evaluateToObject(expression, this.itemContext);
    }

    public Object  evaluateToObject(String expression,StandardEvaluationContext  itemContext){
        Object exp = expression;
        try {
            if(expression.contains("${")){
                CompositeStringExpression compositeStringExpression = (CompositeStringExpression) parser.parseExpression(expression, new TemplateParserContext());
                for(Expression expression1 : compositeStringExpression.getExpressions()){
                    if(expression1 instanceof SpelExpression){
                        String str  = parser.parseExpression(expression1.getExpressionString(), new TemplateParserContext()).getValue(itemContext).toString();
                        if(str.contains("#")) {
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
