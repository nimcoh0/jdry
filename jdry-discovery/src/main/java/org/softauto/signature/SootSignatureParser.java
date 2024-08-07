package org.softauto.signature;

import org.apache.commons.lang3.StringUtils;

public class SootSignatureParser {

    private String sig;

    private String returnType ;

    private String method;

    private String name;

    private String klass;

    private boolean field = false;

    private String[] parametersType = new String[0] ;

    public String getReturnType() {
        return returnType;
    }

    public String getMethod() {
        return method;
    }

    public String getKlass() {
        return klass;
    }

    public String[] getParametersType() {
        return parametersType;
    }

    public SootSignatureParser setReturnType(String returnType) {
        this.returnType = returnType;
        return this;
    }

    public boolean isField() {
        return field;
    }

    public SootSignatureParser setSig(String sig) {
        this.sig = sig;
        return this;
    }

    //  Signatures.MethodSignature methodSignature = new Signatures.MethodSignature(sig);
    //  methodSignature.parameterTypes();

    // TraceSignatureVisitor signatureVisitor = new TraceSignatureVisitor(0);
    //  SignatureReader signatureReader = new SignatureReader(sig);
    //  signatureReader.accept(signatureVisitor);
    //  signatureVisitor.visitClassBound();

    public SootSignatureParser parse(){
        String s = StringUtils.substringBetween(sig,"<",">");
        if(s != null) {
            klass = s.substring(0, s.indexOf(":"));
            s = s.substring(s.indexOf(":") + 1).trim();
            returnType = s.substring(0, s.indexOf(" "));
            s = s.substring(s.indexOf(" ") + 1);
            if (s.contains("(")) {
                method = s.substring(0, s.indexOf("("));
                s = StringUtils.substringBetween(s, "(", ")");
                if(!s.isEmpty()) {
                    parametersType = s.split(",");
                }
            } else {
                name = s;
                field = true;
            }
        }
        return this;
    }

}
