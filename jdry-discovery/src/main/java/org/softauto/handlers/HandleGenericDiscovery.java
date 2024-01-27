package org.softauto.handlers;

import org.apache.commons.lang3.StringUtils;
import org.softauto.flow.FlowObject;
import soot.SootClass;
import soot.tagkit.ParamNamesTag;
import soot.tagkit.SignatureTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class HandleGenericDiscovery {

    List<Tag> tags;

    FlowObject flowObject;

    String globalGeneric;

    public String getGlobalGeneric() {
        return globalGeneric;
    }

    public HandleGenericDiscovery setFlowObject(FlowObject flowObject) {
        this.flowObject = flowObject;
        return this;
    }

    public HandleGenericDiscovery setTags(List<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public HandleGenericDiscovery build() {
        parseClassType();
        if(tags != null && tags.size() > 0) {
            for (Tag tag : tags) {
                if (tag instanceof SignatureTag) {
                    SignatureTag t = (SignatureTag) tag;
                    parser(t.getSignature());
                   // parserReturnType(t.getSignature());
                    return this;
                }else if(tag instanceof ParamNamesTag){
                    ParamNamesTag t = (ParamNamesTag) tag;
                    for(int i=0;i<t.getInfo().size();i++){
                        flowObject.addArgsType(i,false);
                    }
                    return this;
                }
            }
        }
        return this;
    }

    private void parseClassType(){
        SootClass c = flowObject.getMethod().getDeclaringClass();
        List<Tag> tags = c.getTags();
        for(Tag tag :tags ){
            if (tag instanceof SignatureTag) {
                SignatureTag t = (SignatureTag) tag;
                String string =  StringUtils.substringBetween(t.getSignature(),"<",">");
                if(string.startsWith("T")){
                    globalGeneric =  string.substring(3,string.length()-1).replace("/",".");
                }
            }
        }
    }

    private void parserReturnType(String signature){
        String string =  signature.substring(signature.lastIndexOf(")"),signature.length());
        if(string.contains("T")){
            flowObject.setReturnTypeGeneric(true);
        }
        flowObject.setReturnTypeGeneric(false);
    }

    private void parser(String signature){
       String string =  StringUtils.substringBetween(signature,"(",")");
       String[] args = StringUtils.split(string,";");
       for(int i=0;i<args.length;i++){
           if(args[i].startsWith("T")){
               flowObject.addArgsType(i,true);
           }else {
               flowObject.addArgsType(i,false);
           }
       }
    }
}
