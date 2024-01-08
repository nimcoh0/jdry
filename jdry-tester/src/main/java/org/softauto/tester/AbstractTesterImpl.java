package org.softauto.tester;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.analyzer.model.scenario.Scenario;
import org.softauto.core.*;
import org.softauto.listener.ListenerServerProviderImpl;
import org.softauto.plugin.ProviderManager;
import org.softauto.plugin.spi.PluginProvider;
import org.softauto.system.SystemState;
import org.testng.*;
import org.testng.annotations.BeforeTest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * main test class
 */
public class AbstractTesterImpl  {

    private static final Logger logger = LogManager.getLogger(AbstractTesterImpl.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    public ObjectMapper mapper = new ObjectMapper();

    public Scenario scenario;

    public String scenarioId;

    public AbstractTesterImpl(){
        try {
            mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        }catch (Exception e){
            logger.error(JDRY,"fail start AbstractTester Impl ",e);
        }
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
            TestContext.setScenario(scenario);
            TestContext.setTestContext(testContext);
            SystemState.getInstance().initialize(scenario);
            //addJarToClasspath();
            loadPlugins();
        } catch (IOException e) {
           logger.error(JDRY,"fail before Scenario",e);
        }
    }


        public void loadPlugins () {
            try {
                for (PluginProvider provider : ProviderManager.providers()) {
                    provider.create().initialize().register();
                }
            } catch (Exception e) {
                logger.error(JDRY,"fail to load plugins  ", e);
            }
        }


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

    public static void addJarToClasspath() {
        try {
            List<String> f = Configuration.get("jar_path").asList();
            for(String file : f) {
                ClassLoader classLoader = ClassLoader.getSystemClassLoader();
                addURL(file, classLoader);
            }
        } catch (Exception e) {
            logger.error(JDRY,"fail add Jar To Classpath ",e);
        }
    }

    public static void addURL(String path,ClassLoader sysloader)  {
        try {
            Method method = sysloader.getClass()
                    .getDeclaredMethod("appendToClassPathForInstrumentation", String.class);
            method.setAccessible(true);
            method.invoke(sysloader, path);
        } catch (Throwable t) {
            logger.error(JDRY,"fail add jar to classpath",t);
        }
    }

}