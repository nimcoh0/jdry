package org.softauto.signature;


public class VarType {

    String var;

    public VarType setVar(String var) {
        this.var = var;
        return this;
    }


    public String apply(Class c) {
        try {
            Object o = c.getConstructors()[0].newInstance();
            return (String) ((TypeInterface)o).apply(var);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
