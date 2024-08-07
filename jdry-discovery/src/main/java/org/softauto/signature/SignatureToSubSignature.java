package org.softauto.signature;

public class SignatureToSubSignature {

    String sig;

    String clazz;

    String subSignature;

    public String getClazz() {
        return clazz;
    }

    public String getSubSignature() {
        return subSignature;
    }

    public SignatureToSubSignature setSig(String sig) {
        this.sig = sig;
        return this;
    }

    public SignatureToSubSignature parse(){
        clazz = sig.substring(1,sig.indexOf(":")).trim();
        subSignature = sig.substring(sig.indexOf(":")+1,sig.indexOf("-")-1).trim();
        return this;
    }




}
