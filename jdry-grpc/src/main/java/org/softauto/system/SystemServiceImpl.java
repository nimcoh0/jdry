package org.softauto.system;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
//import org.softauto.core.Configuration;
//import org.softauto.core.Context;
import org.softauto.analyzer.model.scenario.Scenario;
import org.softauto.core.Configuration;
import org.softauto.core.TestContext;
import org.softauto.core.TestLifeCycle;
import org.softauto.injector.InjectorInitializer;
import org.softauto.listener.manager.ListenerClientProviderImpl;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * System service for internal messages between the listener server and the grpc server
 */
public class SystemServiceImpl {

    private static SystemServiceImpl systemServiceImpl = null;

    /** indecate if the syatem was loaded */
    boolean loaded = false;

    //HashMap<String, HashMap<String,Object>> scenarios = new HashMap<>();

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(SystemServiceImpl.class);

    private static final Marker TRACER = MarkerManager.getMarker("TRACER");

    public static SystemServiceImpl getInstance(){
        if(systemServiceImpl == null){
            systemServiceImpl = new SystemServiceImpl();
        }
        return systemServiceImpl;
    }

    private SystemServiceImpl(){

    }

    public boolean hello(String scenarioId) {

        return true;
    }



    /**
     * set start test log
     * @param testname
     */
    public boolean startTest(String scenarioId,String testname){
        TestContext.setTestState(TestLifeCycle.START);
        logger.info(" **************** start test "+ testname+" "+ scenarioId+ " ******************");
        //logger.info(TRACER," **************** start test "+ testname+ " ******************");
        return true;
    }

    /**
     * set end test log
     * @param testname
     */
    public boolean endTest(String scenarioId,String testname){
        try {
            TestContext.setTestState(TestLifeCycle.STOP);
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            Date date = new Date(System.currentTimeMillis());
            String d = formatter.format(date);
            System.setProperty("logFilename", testname+"_"+d);
            logger.info(" **************** end test " + testname +" "+ scenarioId+ " ******************");
            //logger.info(TRACER, " **************** end test " + testname + " ******************");
            logger.info(TRACER, "roll test");

        }catch (Exception e){
            logger.error("fail end test ",e);
        }
        return true;
    }


    public boolean configuration(String scenarioId,Scenario scenario) {
        try {
            Scenarios.addScenario(scenarioId,scenario);
            Scenarios.getScenario(scenarioId).setTestState(TestLifeCycle.INITIALIZE);
            Configuration.addConfiguration(scenario.getConfiguration());
            if (!loaded) {
                load();
                loaded = true;
            }
        }catch (Exception e){
            logger.error("fail set configuration  ",e);
        }
        logger.debug("successfully  set configuration  ");
        return true;
    }

    public void shutdown() {

    }

    private  void load()  {
        try {
            InjectorInitializer.getInstance().initialize();
            ListenerClientProviderImpl.getInstance().initialize().register();
        }catch(Throwable e){
            logger.error("init fail ",e);

        }
    }

}
