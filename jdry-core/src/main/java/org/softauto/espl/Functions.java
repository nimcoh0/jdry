package org.softauto.espl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.CompositeStringExpression;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.expression.spel.SpelNode;
import org.springframework.expression.spel.ast.*;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Functions {

    static SpelExpressionParser parser = new SpelExpressionParser();

    public static String step(String expression){
        LinkedList<String> list = new LinkedList<>();
        String result = null;
        try {
            //HashMap<String,Object> hm  = new HashMap<>();
            CompositeStringExpression exp = (CompositeStringExpression) parser.parseExpression(expression,new TemplateParserContext());
            Expression[] expressions = exp.getExpressions();
            for(Expression e : expressions){
                if(e instanceof LiteralExpression){
                    list.add(e.getExpressionString());
                }
                if(e instanceof SpelExpression){
                    list.add(((SpelExpression) e).getExpressionString());
                }
            }
            result = new ObjectMapper().writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return  result;
    }


}
