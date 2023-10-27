package org.softauto.analyzer.core.system.espl;

public class EsplFunctions {


    public static Object exec(String expression){
       return Espl.getInstance().evaluate(expression);
    }

    public static Object run(String expression,Object context){
        return Espl.getInstance().addRootObject(context).evaluate(expression);
    }

    public static Object step(String expression){
        return "dsfgh";
    }

}


