package org.softauto.tester;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.softauto.analyzer.model.scenario.Scenario;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.analyzer.model.scenario.Scenario;
import org.softauto.core.*;
//import org.softauto.deserializer.NullStringJsonDeserializer;
import org.softauto.espl.Espl;
import org.softauto.listener.ListenerServerProviderImpl;
import org.softauto.plugin.ProviderManager;
import org.softauto.plugin.spi.PluginProvider;
//import org.softauto.serializer.HttpServletRequestScenarioIdSerializer;
import org.softauto.system.SystemState;
import org.testng.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.io.IOException;
import java.util.*;


public class AbstractTesterImpl  {

    private static final Logger logger = LogManager.getLogger(AbstractTesterImpl.class);

    public ObjectMapper mapper = new ObjectMapper();

    private static final Marker TESTER = MarkerManager.getMarker("TESTER");

    public Scenario scenario;

    //public Test test;

    //private static TestContext jdryTestContext = new TestContext();

    public String currentUser = null;

    public Espl espl = Espl.reset();

    public String scenarioId;

    //public String sessionId;

    public AbstractTesterImpl(){
        try {

            mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
          // SimpleModule module = new SimpleModule();
           //sessionId = UUID.randomUUID().toString();
           //TestContext.put("sessionId",sessionId);

           //module.addDeserializer(String.class, new NullStringJsonDeserializer());
           //mapper.registerModule(module);
           //SystemState.getInstance().initialize(scenario);
           //ListenerServerProviderImpl.getInstance().initialize().register();
           //loadPlugins();
        }catch (Exception e){
            logger.error("fail start listener ",e);
        }
    }



    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    @BeforeTest
    public void beforeScenario(ITestContext testContext) {
        try {
           TestContext.restart();
           if(scenario == null) {
                scenario = new Scenario();
                scenarioId = UUID.randomUUID().toString();
                scenario.setId(scenarioId);
                scenario.setSuiteName(testContext.getSuite().getName());
            }
            scenario.setState(ScenarioLifeCycle.START.name());
            //scenario.setConfiguration(Configuration.getConfiguration());
            TestContext.setScenario(scenario);
            TestContext.setTestContext(testContext);
            SystemState.getInstance().initialize(scenario);
            //scenario.setConfiguration(Configuration.getConfiguration());
            ListenerServerProviderImpl.getInstance().initialize().register();
            loadPlugins();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        @BeforeMethod
        public void beforeMethod (ITestContext testContext){
            //scenario.getId();
            //test = new ListenerService();
           // test.setId(testContext.getName());

        }

        public void loadPlugins () {
            try {
                for (PluginProvider provider : ProviderManager.providers()) {
                    provider.create().initialize().register();
                }
            } catch (Exception e) {
                logger.error("fail to load plugins  ", e);
            }
        }


    private static final Set<String> ExcludeMargeCallOption = new HashSet<>(
            Arrays.asList("AUTH"));



    public JsonNode toJsonNode(Object o){
        try {
            if(o instanceof String && Utils.isJson(o.toString())){
                return new ObjectMapper().readTree(o.toString());
            }
            String str = new ObjectMapper().writeValueAsString(o);
            return new ObjectMapper().readTree(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}