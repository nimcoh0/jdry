package org.softauto.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.model.scenario.Scenario;
import org.softauto.core.Configuration;
import org.softauto.core.ScenarioLifeCycle;
import org.softauto.core.StepLifeCycle;
import org.softauto.listener.manager.ListenerClientProviderImpl;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * System service for internal messages between the listener server and the grpc server
 */
public class SystemServiceImpl {

    private static SystemServiceImpl systemServiceImpl = null;

    /** indecate if the syatem was loaded */
    boolean loaded = false;



    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(SystemServiceImpl.class);

    private static final Marker TRACER = MarkerManager.getMarker("TRACER");

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

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
        Scenarios.getScenario(scenarioId).setStepState(StepLifeCycle.START);
        Scenarios.getScenario(scenarioId).setScenarioState(ScenarioLifeCycle.START);
        logger.info(JDRY," **************** start test "+ testname+" "+ scenarioId+ " ******************");
        return true;
    }

    /**
     * set end test log
     * @param testname
     */
    public boolean endTest(String scenarioId,String testname){
        try {
            Scenarios.getScenario(scenarioId).setScenarioState(ScenarioLifeCycle.STOP);
            Scenarios.getScenario(scenarioId).setStepState(StepLifeCycle.STOP);
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            Date date = new Date(System.currentTimeMillis());
            String d = formatter.format(date);
            System.setProperty("logFilename", testname+"_"+d);
            logger.info(JDRY," **************** end test " + testname +" "+ scenarioId+ " ******************");
            logger.info(TRACER, "roll test");

        }catch (Exception e){
            logger.error(JDRY,"fail end test ",e);
        }
        return true;
    }


    public boolean configuration(String scenarioId, String json) {
        try {
            Scenario scenario = new ObjectMapper().readValue(json,Scenario.class);
            Scenarios.addScenario(scenarioId,scenario);
            Scenarios.getScenario(scenarioId).setScenarioState(ScenarioLifeCycle.INITIALIZE);
            Configuration.addConfiguration(scenario.getConfiguration());
            if (!loaded) {
                load();
                loaded = true;
            }
        }catch (Exception e){
            logger.error(JDRY,"fail set configuration  ",e);
        }
        logger.debug(JDRY,"successfully  set configuration  ");
        return true;
    }

    public void shutdown() {

    }

    private  void load()  {
        try {
            ListenerClientProviderImpl.getInstance().initialize().register();
        }catch(Throwable e){
            logger.error(JDRY,"init fail ",e);

        }
    }

}
