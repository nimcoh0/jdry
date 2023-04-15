package org.softauto.tester;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.listener.Exec;
import org.softauto.listener.ListenerObserver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

public class Test {

    private static Logger logger = LogManager.getLogger(Test.class);

    Object result;

    Object expected;

    String id;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getExpected() {
        return expected;
    }

    public void setExpected(Object expected) {
        this.expected = expected;
    }

    public static Listener resetListeners()throws Exception{
        //SystemState.getInstance().resetListeners();
        ListenerObserver.getInstance().reset();
        logger.debug("reset Listeners successfully");
        return  new Listener();
    }

    public static Listener removeListener(String fqmn, Class...types)throws Exception{
        //SystemState.getInstance().removeListener(fqmn,types);
        ListenerObserver.getInstance().unRegister(fqmn);
        logger.debug("remove Listener successfully "+ fqmn);
        return  new Listener();
    }


    public static Listener addListener(String fqmn, Class...types)throws Exception{
        //SystemState.getInstance().addListener(fqmn,types);
        logger.debug("add Listener successfully "+ fqmn+ " types "+ Arrays.toString(types));
        return  new Listener().setFqmn(fqmn);
    }

    public static Listener addListener(Function function,String fqmn, Class...types)throws Exception{
        //SystemState.getInstance().addListener(fqmn,types);
        logger.debug("add Listener successfully "+ fqmn+ " types "+ Arrays.toString(types));
        Exec func = new Exec(function,fqmn);
        ListenerObserver.getInstance().register(fqmn,func);
        return  new Listener().setFqmn(fqmn).setFunc(func);
    }


    public static Listener addListener(String fqmn,Object[] value, Class...types)throws Exception{
        //SystemState.getInstance().addListener(fqmn,value,types);
        logger.debug("add Listener im mock mode successfully "+ fqmn+ " types "+ Arrays.toString(types));
        return  new Listener().setFqmn(fqmn);
    }

    public static Listener addListener(Function function,String fqmn,Object[] value, Class...types)throws Exception{
        //SystemState.getInstance().addListener(fqmn,value,types);
        logger.debug("add Listener im mock mode successfully "+ fqmn+ " types "+ Arrays.toString(types));
        Exec func = new Exec(function,fqmn);
        ListenerObserver.getInstance().register(fqmn,func);
        return  new Listener().setFqmn(fqmn).setFunc(func);
    }




    /*
    public static Listener addListeners(HashMap<String,Class[]> listeners)throws Exception{
        listeners.forEach((fqmn,types)-> {
            try {
                SystemState.getInstance().addListener(fqmn, types);
                logger.debug("add Listener successfully "+ fqmn+ " types "+ Arrays.toString(types));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return  new Listener();
    }

     */

}
