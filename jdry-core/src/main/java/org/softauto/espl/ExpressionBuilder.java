package org.softauto.espl;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class ExpressionBuilder {

    static String operator;
    static List<Expression> expressions = new ArrayList<>();

    public  Expression newExpression() { return new Expression();}

    public  ExpressionBuilder newExpression(String expression) {
       Expression exp = new Expression();
       exp.setContext(expression.substring(0,expression.indexOf(".")));
       exp.setStatement(expression.substring(expression.indexOf(".")+1,expression.length()));
       expressions.add(exp);
       return this;
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

    public ExpressionBuilder(){
        expressions = new ArrayList<>();
        operator = null;
    }

    public ExpressionBuilder(Expression expression){
        expressions.add(expression);
    }

    public ExpressionBuilder setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public String toJson()throws Exception{
        return new ObjectMapper().writeValueAsString(this);
    }

    public String getOperator() {
        return operator;
    }

    public ExpressionBuilder build(){
        return this;
    }

    public static class Expression {
        Object context;

        String statement;

        public Expression setContext(Object context) {
            this.context = context;
            return this;
        }

        public Expression setStatement(String statement) {
            this.statement = statement;
            return this;
        }

        public Object getContext() {
            return context;
        }

        public String getStatement() {
            return statement;
        }

        public String toString(){
            try{
               return new ObjectMapper().writeValueAsString(this);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        public ExpressionBuilder build(){
            return new ExpressionBuilder(this);
        }
    }


}
