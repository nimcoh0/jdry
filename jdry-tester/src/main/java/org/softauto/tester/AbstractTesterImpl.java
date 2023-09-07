package org.softauto.tester;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.core.*;
import org.softauto.deserializer.NullStringJsonDeserializer;
import org.softauto.espl.Espl;
import org.softauto.functions.SpecialFunctions;
import org.softauto.listener.ListenerServerProviderImpl;
import org.softauto.plugin.ProviderManager;
import org.softauto.plugin.spi.PluginProvider;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.util.*;


public class AbstractTesterImpl extends SpecialFunctions {

    private static final Logger logger = LogManager.getLogger(AbstractTesterImpl.class);

    public ObjectMapper mapper = new ObjectMapper();

    public Test test;

    public String currentUser = null;

    public Espl espl = Espl.reset();

    public String scenarioId;

    public AbstractTesterImpl(){
        try {
           // SecurityManager sm = new MySecurityManager();
           // System.setSecurityManager(sm);
           mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
           SimpleModule module = new SimpleModule();
           module.addDeserializer(String.class, new NullStringJsonDeserializer());
           mapper.registerModule(module);
           //Resolver.getInstance().clean();
           SystemState.getInstance().initialize();
           ListenerServerProviderImpl.getInstance().initialize().register();
           loadPlugins();
           suite = (AbstractSuite)SuiteFactory.getSuite(Configuration.get(Context.CACHE_IMPL).asString());
          // loadTestSuite();
        }catch (Exception e){
            logger.error("fail start listener ",e);
        }
    }

    @BeforeTest
    public void beforeScenario(ITestContext testContext) {
        scenarioId = UUID.randomUUID().toString();
        TestContext.addScenario(scenarioId);

    }




    @BeforeMethod
    public void beforeMethod(ITestContext testContext){
        test = new Test();
        test.setId(testContext.getName());

    }

    public  void loadPlugins(){
        try {
            for(PluginProvider provider : ProviderManager.providers()){
                  provider.create().initialize().register();
            }
        }catch (Exception e){
            logger.error("fail to load plugins  ",e);
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