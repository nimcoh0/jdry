package org.softauto.signature;

import org.apache.commons.lang3.StringUtils;

public class MethodSignatureParser {


    private static String sig;

    private String className;

    private String signature;

    private int index = -1;

    public MethodSignatureParser(){

    }


    public static MethodSignatureParser setSig(String sig) {
        MethodSignatureParser.sig = sig;
        return new MethodSignatureParser();
    }

    public int getIndex() {
        return index;
    }

    public String getClassName() {
        return className;
    }

    public String getSignature() {
        return signature;
    }

    public MethodSignatureParser build(){
        if(sig.contains("index=")){
            index = Integer.valueOf(sig.substring(sig.indexOf("index=")+6));
        }
        sig = StringUtils.substringBetween(sig,"<",">");
        if(sig != null) {
            className = sig.substring(0, sig.indexOf(":")).trim();
            signature = sig.substring(sig.indexOf(":") + 1, sig.length()).trim();
        }
        return this;
    }
}
