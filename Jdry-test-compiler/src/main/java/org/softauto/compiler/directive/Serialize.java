package org.softauto.compiler.directive;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.softauto.analyzer.core.utils.ApplyRule;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class Serialize extends Directive {


    @Override
    public String getName() {
        return "serialize";
    }

    @Override
    public int getType() {
        return LINE;
    }


    public String serialize(String expression,String type){
        return ApplyRule.setRule("serialize").addContext("type", type).addContext("expression",expression).apply().getResult();
    }

    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        StringWriter expression = new StringWriter();
        StringWriter type = new StringWriter();
        StringWriter varName = new StringWriter();
        if(node.jjtGetNumChildren() > 0) {
            node.jjtGetChild(0).render(context, expression);
            node.jjtGetChild(1).render(context, type);
            node.jjtGetChild(2).render(context, varName);
            Object result = serialize(expression.toString(),type.toString());
            if (result != null) {
                if(varName != null){
                    result = "String "+ varName +" = "+result.toString() +"\r\n";
                }
                writer.write(result.toString());
                return true;
            }
        }
        return false;
    }
}
