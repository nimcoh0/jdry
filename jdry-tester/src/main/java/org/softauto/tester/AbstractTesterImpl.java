package org.softauto.tester;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.core.*;
import org.softauto.deserializer.NullStringJsonDeserializer;
import org.softauto.listener.ListenerServerProviderImpl;
import org.softauto.plugin.ProviderManager;
import org.softauto.plugin.ProviderScope;
import org.softauto.plugin.spi.PluginProvider;
import org.testng.IInvokedMethod;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;


public class AbstractTesterImpl {

    private static final Logger logger = LogManager.getLogger(AbstractTesterImpl.class);

    public ObjectMapper mapper = new ObjectMapper();

    public Test test;

    public Suite suite = new Suite();




    public AbstractTesterImpl(){
        try {
            SecurityManager sm = new MySecurityManager();
            System.setSecurityManager(sm);
           mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
            SimpleModule module = new SimpleModule();
            module.addDeserializer(String.class, new NullStringJsonDeserializer());
            mapper.registerModule(module);
           //SimpleModule module = new SimpleModule();
           //module.addDeserializer(NullNode.class, new JsonNodeDeserializer());
           //mapper.registerModule(module);
           Resolver.getInstance().clean();
           SystemState.getInstance().initialize();
           ListenerServerProviderImpl.getInstance().initialize().register();
           loadPlugins();

          // loadTestSuite();
        }catch (Exception e){
            logger.error("fail start listener ",e);
        }
    }



    @BeforeMethod
    public void beforeMethod(ITestContext testContext){
        test = new Test();
        test.setId(testContext.getName());

    }

    public  void loadPlugins(){
        try {
            for(PluginProvider provider : ProviderManager.providers(ProviderScope.Tester)){
                  provider.create().initialize().register();
            }
        }catch (Exception e){
            logger.error("fail to load plugins  ",e);
        }
    }


    private static final Set<String> ExcludeMargeCallOption = new HashSet<>(
            Arrays.asList("AUTH"));




    public HashMap<String,Object> margeCallOption(String dependencieId, HashMap<String,Object> callOption){
        HashMap<String,Object> dependenciePublish = suite.getPublish(dependencieId);
        JsonNode dependencieCallOption = (JsonNode) dependenciePublish.get("callOption");
        Map<String, Object> map = new ObjectMapper().convertValue(dependencieCallOption, new TypeReference<Map<String, Object>>() {});

        for(Map.Entry entry : map.entrySet()) {
            if (!callOption.containsKey(entry.getKey())) {
                if (!ExcludeMargeCallOption.contains(entry.getValue().toString()))
                    callOption.put(entry.getKey().toString(), entry.getValue());
            }
        }
        for(Map.Entry entry : callOption.entrySet()) {
            if(entry.getValue().toString().contains("${")){
                String v = Utils.getVar(entry.getValue().toString());
                String dependencieValue = map.get(v).toString();
                String value = entry.getValue().toString().replace("${"+v+"}",dependencieValue);
                callOption.put(v,value);
            }
        }

        return callOption;
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

}