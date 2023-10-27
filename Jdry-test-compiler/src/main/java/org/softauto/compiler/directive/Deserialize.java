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

public class Deserialize extends Directive {


    @Override
    public String getName() {
        return "deserialize";
    }

    @Override
    public int getType() {
        return LINE;
    }


    public String deserialize(String expression,String type,String var,String name){
        return ApplyRule.setRule("deserialize").addContext("type", type).addContext("expression",expression).addContext("var",var).addContext("name",name).apply().getResult();
    }

    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        StringWriter expression = new StringWriter();
        StringWriter type = new StringWriter();
        StringWriter varName = new StringWriter();
        StringWriter name = new StringWriter();
        if(node.jjtGetNumChildren() > 0) {
            node.jjtGetChild(0).render(context, expression);
            node.jjtGetChild(1).render(context, type);
            node.jjtGetChild(2).render(context, varName);
            node.jjtGetChild(3).render(context, name);
            Object result = deserialize(expression.toString(),type.toString(),varName.toString(),name.toString());
            if (result != null) {
                writer.write(result.toString()+"\r\n");
                return true;
            }
        }
        return false;
    }
}
