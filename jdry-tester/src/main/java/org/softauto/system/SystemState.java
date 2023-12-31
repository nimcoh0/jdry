package org.softauto.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.analyzer.model.scenario.Scenario;
import org.softauto.core.*;
import org.softauto.core.DefaultConfiguration;
import org.softauto.listener.ListenerObserver;
import org.softauto.listener.ListenerServerProviderImpl;
import org.softauto.tester.InvocationHandler;
import org.yaml.snakeyaml.Yaml;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;



public class SystemState {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(SystemState.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    private static SystemState systemState = null;

    private SystemState(){};
    Yaml yaml = new Yaml();
    static ObjectMapper objectMapper;

    String scenarioId;

    Scenario scenario;

    public static SystemState getSystemState() {
        return systemState;
    }

    public static SystemState getInstance(){
        if(systemState == null){
            systemState = new SystemState();
            objectMapper = new ObjectMapper(new YAMLFactory());
            loadDefaultConfiguration();
        }
        return systemState;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void initialize(Scenario scenario) throws IOException {
        this.scenarioId = scenario.getId();
        this.scenario = scenario;
        loadConfiguration();
        scenario.setConfiguration(Configuration.getConfiguration());
        if(Configuration.get(Context.TESTER_MODE).asString().equals("agent")) {
            logger.info("load tester in agent mode");
            ListenerServerProviderImpl.getInstance().initialize().register();
            if (sayHello(scenario.getId())) {
                logger.debug(JDRY, "successfully say hello");
                if (sendConfiguration(scenario)) {
                    logger.debug(JDRY, "successfully send configuration");
                } else {
                    logger.error(JDRY, "fail send configuration ");
                }
            } else {
                logger.error(JDRY, "fail say hello ");
            }
        }else {
            logger.info("load tester in agentless mode");
        }
    }


    private Boolean sayHello(String scenarioId){
        try {
            return new org.softauto.tester.InvocationHandler().invoke("org_softauto_system_SystemServiceImpl_hello", new Object[]{scenarioId}, new Class[]{java.lang.String.class});

         }catch (Exception e){
            logger.error(JDRY,"fail sayHello",e);
        }
        return null;
    }

    public Boolean sendConfiguration(Scenario scenario){
        try {
            String json = new ObjectMapper().writeValueAsString(scenario);
            TestContext.getScenario().setState(ScenarioLifeCycle.INITIALIZE.name());
            return new org.softauto.tester.InvocationHandler().invoke("org_softauto_system_SystemServiceImpl_configuration", new Object[]{scenario.getId(), json}, new Class[]{java.lang.String.class,java.lang.String.class});
        }catch (Exception e){
            logger.error(JDRY,"fail send configuration",e);
        }
        return null;
    }


    public Boolean shutdown(String scenarioId) throws Exception{
        return new org.softauto.tester.InvocationHandler().invoke("org_softauto_system_SystemServiceImpl_shutdown", new Object[]{scenarioId}, new Class[]{java.lang.String.class});
    }

    public Boolean startTest(String testname,String scenarioId)throws Exception{
        if(Configuration.get(Context.TESTER_MODE).asString().equals("agent")) {
            ListenerObserver.getInstance().reset();
            boolean r = new org.softauto.tester.InvocationHandler().invoke("org_softauto_system_SystemServiceImpl_startTest", new Object[]{scenarioId, testname}, new Class[]{java.lang.String.class, String.class});

            TestContext.getScenario().setState(ScenarioLifeCycle.START.name());
            return r;
        }
        return true;
    }

    public Boolean endTest(String testname,String scenarioId)throws Exception{
        if(Configuration.get(Context.TESTER_MODE).asString().equals("agent")) {
            boolean r = new InvocationHandler().invoke("org_softauto_system_SystemServiceImpl_endTest", new Object[]{scenarioId, testname}, new Class[]{java.lang.String.class, String.class});
            //TestContext.setStepState(StepLifeCycle.STOP);
            TestContext.getScenario().setState(ScenarioLifeCycle.STOP.name());
            ListenerServerProviderImpl.getInstance().shutdown();
            return r;
        }
        return true;
    }

    public static void loadDefaultConfiguration()  {
        try {
               Configuration.setConfiguration(DefaultConfiguration.getConfiguration());
            logger.debug(JDRY,"default configuration load successfully " + Configuration.getConfiguration());
        }catch(Exception e){
            logger.error(JDRY,"fail load default configuration ",e);
        }
    }

    public SystemState loadConfiguration()  {
        try {
            if(new File(System.getProperty("user.dir")+ "/Configuration.yaml").isFile()) {
                HashMap<String, Object> map = (HashMap<String, Object>) yaml.load(new FileReader(System.getProperty("user.dir") + "/Configuration.yaml"));
                HashMap<String,Object> defaultConfiguration = Configuration.getConfiguration();
                defaultConfiguration.putAll(map);
                Configuration.setConfiguration(defaultConfiguration);
            }
            logger.debug(JDRY,"configuration load successfully " + Configuration.getConfiguration());
        }catch(Exception e){
            logger.error(JDRY,"fail load listener configuration ",e);
        }
        return this;
    }


}
