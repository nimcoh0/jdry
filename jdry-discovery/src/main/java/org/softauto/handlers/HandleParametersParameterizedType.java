package org.softauto.handlers;

import org.apache.commons.lang3.StringUtils;
import org.softauto.flow.FlowObject;
import soot.tagkit.SignatureTag;
import soot.tagkit.Tag;

import java.util.HashMap;
import java.util.List;

public class HandleParametersParameterizedType {

    List<Tag> tags;

    FlowObject flowObject;


    HashMap<Integer,String> parameterizedTypes = new HashMap<>();

    public HashMap<Integer,String> getParameterizedTypes() {
        return parameterizedTypes;
    }

    public HandleParametersParameterizedType setFlowObject(FlowObject flowObject) {
        this.flowObject = flowObject;
        return this;
    }

    public HandleParametersParameterizedType setTags(List<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public HandleParametersParameterizedType build() {
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
        String string =  StringUtils.substringBetween(signature,"(",")");
        String[] s = string.split("(;L)|(;\\[)");
        for(int i=0;i<s.length;i++){
            String s2 = StringUtils.substringBetween(s[i], "<", ">");
            if(s2 != null && !s2.isEmpty()) {
                s2 = StringUtils.substringAfter(s2,"L");
                s2 = StringUtils.substringBefore(s2,";");
                parameterizedTypes.put(i, s2.replace("/", "."));
            }
        }

        //if(string != null)
           // parameterizedType =  string.substring(1,string.length()-1).replace("/",".");


    }
}
