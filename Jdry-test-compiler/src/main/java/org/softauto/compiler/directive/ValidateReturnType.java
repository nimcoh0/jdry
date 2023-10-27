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

public class ValidateReturnType extends Directive {
    @Override
    public String getName() {
        return "validateReturnType";
    }

    @Override
    public int getType() {
        return BLOCK;
    }

    public boolean validate(String type){
        return ApplyRule.setRule("validate_return_type").addContext("type", type).apply().getResult();
    }


    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        StringWriter type = new StringWriter();
        StringWriter content = new StringWriter();
        if(node.jjtGetNumChildren() > 0) {
            node.jjtGetChild(0).render(context, type);
            node.jjtGetChild(1).render(context, content);
            Object result = validate(type.toString());
            if (result != null) {
                if((boolean)result){
                    writer.write(content.toString());
                }
                return true;
            }
        }
        return false;
    }
}
