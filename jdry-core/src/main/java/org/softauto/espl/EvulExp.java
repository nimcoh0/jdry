package org.softauto.espl;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.TypeComparator;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.spel.support.StandardTypeComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvulExp {

    ExpressionBuilder exp;
    Map<String, Object> contexts = new HashMap<>();

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(EvulExp.class);

    public Map<String, Object> getContexts() {
        return contexts;
    }

    public EvulExp setExp(ExpressionBuilder exp) {
        this.exp = exp;
        return this;
    }

    public EvulExp setContexts(Map<String, Object> localParams) {
        this.contexts = localParams;
        return this;
    }

    public EvulExp addContexts(Map<String, Object> localParams) {
        this.contexts.putAll(localParams);
        return this;
    }

    public EvulExp addContexts(String key,Object value) {
        this.contexts.put(key,value);
        return this;
    }



    public synchronized <T> T evulExp (String exp)throws Exception{
        //logger.debug("evaluate "+exp +" using object context "+ ctx.getClass().getName());
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext itemContext = new StandardEvaluationContext();
        if(itemContext == null){
            throw new Exception("fail to evul " + exp );
        }
        Expression exp2 = parser.parseExpression(exp);
        T res = (T) exp2.getValue(itemContext);
        logger.debug("evaluate "+exp +" to "+ res);
        return res;
    }



    /*
    public synchronized Class evulClass (String exp)throws Exception{
        //logger.debug("evaluate "+exp +" using object context "+ ctx.getClass().getName());
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp2 = parser.parseExpression("T("+exp+")");
        return exp2.getValue(Class.class);

    }


    public synchronized Object evulExp1 (String exp)throws Exception{
        //logger.debug("evaluate "+exp +" using object context "+ ctx.getClass().getName());
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext itemContext = new StandardEvaluationContext(contexts);
        if(itemContext == null){
            throw new Exception("fail to evul " + exp );
        }
        Expression exp2 = parser.parseExpression(exp);
        Object res =  exp2.getValue(itemContext,contexts);
        logger.debug("evaluate "+exp +" to "+ res);
        return res;
    }


     */
    public synchronized <T> T evulExp (String exp,T ctx)throws Exception{
        logger.debug("evaluate "+exp +" using object context "+ ctx.getClass().getName());
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext itemContext = new StandardEvaluationContext(ctx);
        if(itemContext == null){
            throw new Exception("fail to evul " + exp );
        }
        Expression exp2 = parser.parseExpression(exp);
        T res = (T) exp2.getValue(itemContext,ctx);
        logger.debug("evaluate "+exp +" to "+ res);
        return res;
    }


    /*
    public  Object evulExp1 (String exp,Object ctx)throws Exception{
        logger.debug("evaluate "+exp +" using object context "+ ctx.getClass().getName());
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext itemContext = new StandardEvaluationContext(ctx);
        if(itemContext == null){
            throw new Exception("fail to evul " + exp );
        }
        Expression exp2 = parser.parseExpression(exp);
        Object res = (Object) exp2.getValue(itemContext,Object.class);
        logger.debug("evaluate "+exp +" to "+ res);
        return res;
    }

    public  synchronized Object evulExp (Object o1,Object o2)throws Exception{
        TypeComparator comparator = new StandardTypeComparator();
        int i = comparator.compare(o1,o2);
        logger.debug("comparator result "+ i);
        if(i == 0){
            return true;
        }
        return false;

    }


     */
    public synchronized Object evaluate(){
        try{
            List<Object> objects = new ArrayList<>();
            for(ExpressionBuilder.Expression exp : exp.getExpressions()){
                Object ctx = exp.getContext();
                if(exp.getContext() instanceof String){
                    logger.debug("context is string ."+exp.getContext()+" getting Object from local param");
                    ctx = contexts.get(exp.getContext());
                }else {
                    logger.debug("context is Object  ."+ctx);
                }
                if(ctx != null) {
                    logger.debug("adding expression to the list " + exp.getStatement() + " with context " + ctx.getClass().getName());
                    objects.add(evulExp(exp.getStatement(), ctx));
                }
            }
            //logger.debug("evaluating "+objects.size() + "expressions ");
            //if(objects.size() <= 2) {
                /*
                if (objects.size() > 1) {
                    logger.debug("evaluate expression " +exp.getExpressions().get(0).toString()+" evaluate to "+objects.get(0).toString() + " " + exp.getOperator()+" "+exp.getExpressions().get(1).toString()+" evaluate to "+objects.get(1).toString());
                    return evulExp(objects.get(0),objects.get(01));
                } else

                 */
                if (objects != null && objects.size() > 0) {
                    logger.debug("evaluate expression " + exp.getExpressions().get(0).toString()+ "result: "+ objects.get(0));
                    return  objects.get(0);
                }
                logger.error("no Objects to evaluate ");
                return exp.getExpressions().get(0).getContext()+"."+exp.getExpressions().get(0).getStatement();
           // }else{
             //   logger.error("number of expressions > 2 not supported ");
              //  return false;
            //}
        }catch (Exception e){
            logger.error("fail evaluate expression ", e);
        }
        return exp.getExpressions();
    }
}
