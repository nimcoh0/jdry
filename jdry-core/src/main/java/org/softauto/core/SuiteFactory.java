package org.softauto.core;

public class SuiteFactory {

    public static AbstractSuite abstractSuite;

    private SuiteFactory(){}

    public static AbstractSuite getSuite(String impl){
        try {
            Class c = Class.forName(Configuration.get(Context.CACHE_IMPL).asString());
            return abstractSuite = (AbstractSuite)c.getConstructors()[0].newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AbstractSuite getSuite(){
        return abstractSuite;
    }

}
