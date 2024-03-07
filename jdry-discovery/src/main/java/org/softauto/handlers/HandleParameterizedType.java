package org.softauto.handlers;

import org.apache.commons.lang3.StringUtils;
import org.softauto.flow.FlowObject;
import soot.SootClass;
import soot.tagkit.ParamNamesTag;
import soot.tagkit.SignatureTag;
import soot.tagkit.Tag;

import java.util.List;

public class HandleParameterizedType {

    List<Tag> tags;

    FlowObject flowObject;

    String parameterizedType;

    public String getParameterizedType() {
        return parameterizedType;
    }

    public HandleParameterizedType setFlowObject(FlowObject flowObject) {
        this.flowObject = flowObject;
        return this;
    }

    public HandleParameterizedType setTags(List<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public HandleParameterizedType build() {
        if(tags != null && tags.size() > 0) {
            for (Tag tag : tags) {
                if (tag instanceof SignatureTag) {
                    SignatureTag t = (SignatureTag) tag;
                    parser(t.getSignature());

                    return this;
                }
            }
        }
        return this;
    }





    private void parser(String signature){
       String string =  signature.substring(signature.indexOf(")"));
       string =  StringUtils.substringBetween(string,"<",">");
       string = StringUtils.substringAfter(string,"L");
       parameterizedType =   StringUtils.substringBefore(string,"<").replace("/",".");
       //parameterizedType =  string.substring(1,string.length()-1).replace("/",".");

    }
}
