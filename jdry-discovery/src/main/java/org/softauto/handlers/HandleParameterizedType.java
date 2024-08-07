package org.softauto.handlers;

import org.apache.commons.lang3.StringUtils;
import org.softauto.flow.FlowObject;
import org.softauto.signature.DescToNativeSignature;
import org.softauto.signature.SignatureParser;
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

    private String getResponseType(String s){
        String string = StringUtils.substringAfter(s,"L");
        string =  StringUtils.substringBefore(string,"<");
        return string.replace("/",".");
    }


    private void parser(String signature){
       //String string =  signature.substring(signature.indexOf(")"));
       //String s = getResponseType(string);
        DescToNativeSignature signatureParser = new DescToNativeSignature().setSig(signature).parse();
       //if(flowObject.getUnboxReturnType() == null || s.equals(flowObject.getUnboxReturnType())) {
          // string = string != null ?  StringUtils.substringBetween(string,"<",">") : null;
          // string = string != null ?  StringUtils.substringAfter(string, "L"): null;
          // string = string != null ?  StringUtils.substringBefore(string, ";"): null;
         //  string = string != null ?  StringUtils.substringBefore(string, "<"): null;
          // parameterizedType = string != null ?  string.replace("/", "."): null;
        String rt = signatureParser.getReturnType();
        if(rt.contains("<")) {
            //rt = StringUtils.substringBetween(rt, "<", ">");
            rt = rt.substring(rt.indexOf("<") + 1, rt.lastIndexOf(">"));

            if (rt.startsWith("T")) {
                parameterizedType = rt.substring(1, rt.length() - 1).replace("/", ".");
                flowObject.setReturnTypeGeneric(true);
            } else if (rt.startsWith("L")) {
                parameterizedType = rt.substring(1, rt.length() - 1).replace("/", ".");
                flowObject.setReturnTypeGeneric(false);
            } else if (rt.startsWith("+")) {
                parameterizedType = rt.substring(2, rt.length() - 1).replace("/", ".");
                flowObject.setReturnTypeGeneric(false);
            }
            else {
                parameterizedType = rt.replace("/", ".");
                flowObject.setReturnTypeGeneric(false);
            }
        }

        //}
      // parameterizedType =   StringUtils.substringBefore(string,"<").replace("/",".");
       //parameterizedType =  string.substring(1,string.length()-1).replace("/",".");

    }
}
