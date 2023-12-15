package org.softauto.spel;

public class SpelFunctions {


    public static Object exec(String expression){
       return SpEL.getInstance().evaluate(expression);
    }

    public static Object run(String expression,Object context){
        return SpEL.getInstance().addRootObject(context).evaluate(expression);
    }

}


