package org.softauto.compiler.directive;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.softauto.analyzer.core.system.espl.Espl;
import org.softauto.analyzer.core.utils.ApplyRule;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class After extends Directive {

    Writer writer;

    @Override
    public String getName() {
        return "after";
    }

    @Override
    public int getType() {
        return BLOCK;
    }

    public String serialize(Node node,InternalContextAdapter context){
        Object o = null;
        try {
            StringWriter expression = new StringWriter();
            StringWriter type = new StringWriter();
            node.jjtGetChild(0).render(context, expression);
            node.jjtGetChild(1).render(context, type);
            o =  ApplyRule.setRule("after_serialize").addContext("type", type).addContext("expression",expression).apply().getResult();
            writer.write(o.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return o.toString();
    }

    public boolean isValid(String type){
            return  ApplyRule.setRule("after_isValid").addContext("type", type).apply().getResult();
    }



    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        this.writer = writer;
        StringWriter content = new StringWriter();
        if(node.jjtGetNumChildren() > 0) {
            //Object value = node.jjtGetChild(0).getParser().getToken(0);
            node.jjtGetChild(0).render(context, content);
            String method  = content.toString().substring(0,content.toString().indexOf("("));
            String payload =  content.toString().substring(method.toCharArray().length+1,content.toString().indexOf(")"));
            payload =  payload.replaceAll("\"","'");
            String[] args = payload.split(",");
            String arguments = "";
            for(String arg : args){
                if(arguments.isEmpty()){
                    arguments = arguments + "'"+arg+"'";
                }else {
                    arguments = arguments + ",'"+arg+"'";
                }
            }
            Object result = Espl.getInstance().addRootObject(this).evaluate("#this" + method+"("+arguments+")");
            if (result != null) {
                //writer.write(result.toString());
                return true;
            }
        }
        return false;
    }
}
