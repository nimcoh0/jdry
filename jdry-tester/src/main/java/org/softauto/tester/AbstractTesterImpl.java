package org.softauto.tester;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.deserializer.NullStringJsonDeserializer;
import org.softauto.plugin.ProviderManager;
import org.softauto.plugin.ProviderScope;
import org.softauto.plugin.spi.PluginProvider;



public class AbstractTesterImpl {

    private static final Logger logger = LogManager.getLogger(AbstractTesterImpl.class);

    public ObjectMapper mapper = new ObjectMapper();



    public AbstractTesterImpl(){
        try {
           mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
            SimpleModule module = new SimpleModule();
            module.addDeserializer(String.class, new NullStringJsonDeserializer());
            mapper.registerModule(module);
           //SimpleModule module = new SimpleModule();
           //module.addDeserializer(NullNode.class, new JsonNodeDeserializer());
           //mapper.registerModule(module);
           SystemState.getInstance().initialize();
           loadPlugins();
          // loadTestSuite();
        }catch (Exception e){
            logger.error("fail start listener ",e);
        }
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

}