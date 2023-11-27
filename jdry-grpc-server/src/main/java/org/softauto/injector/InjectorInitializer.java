package org.softauto.injector;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.core.Configuration;
//import org.softauto.injector.jvm.HeapHelperInitializer;
import org.yaml.snakeyaml.Yaml;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


/**
 * initilize the injector
 */
public class InjectorInitializer {

    Injector injector = null;
    private static final Logger logger = LogManager.getLogger(InjectorInitializer.class);
    private static InjectorInitializer injectorProviderImpl = null;
    public ObjectMapper objectMapper = null;
    Yaml yaml = new Yaml();

    public static InjectorInitializer getInstance(){
        if(injectorProviderImpl == null){
            injectorProviderImpl =  new InjectorInitializer();
        }
        return injectorProviderImpl;
    }

   private InjectorInitializer(){
       objectMapper = new ObjectMapper();
   }




    public InjectorInitializer initialize() throws IOException {
        try {
                loadConfiguration();
                //HeapHelperInitializer.getInstance().initialize();
                injector = new Injector();
                logger.info("Injector successfully initialize");
        }catch (Throwable e){
            logger.fatal("fail to load injector ", e);
            System.exit(1);
        }
        return this;
    }
    public InjectorInitializer initialize(HashMap<String, Object> map) throws IOException {
        try {
            loadConfiguration(map);
            //HeapHelperInitializer.getInstance().initialize();
            injector = new Injector();
            logger.info("Injector successfully initialize");
        }catch (Throwable e){
            logger.fatal("fail to load injector ", e);
            System.exit(1);
        }
        return this;
    }


    public InjectorInitializer loadConfiguration()  {
        try {
            if(new File(System.getProperty("user.dir")+ "/Configuration.yaml").isFile()) {
                HashMap<String, Object> map = (HashMap<String, Object>) yaml.load(new FileReader(System.getProperty("user.dir") + "/Configuration.yaml"));
                //JsonNode userConfiguration = objectMapper.readTree(new File(System.getProperty("user.dir") + "/Configuration.yaml"));
                HashMap<String,Object> defaultConfiguration = Configuration.getConfiguration();
                defaultConfiguration.putAll(map);
                Configuration.setConfiguration(defaultConfiguration);
            }
            logger.debug("configuration load successfully " + Configuration.getConfiguration());
        }catch(Exception e){
            logger.error("fail load listener configuration ",e);
        }
        return this;
    }

    public InjectorInitializer loadConfiguration(HashMap<String, Object> map )  {
        try {
            HashMap<String,Object> defaultConfiguration = Configuration.getConfiguration();
            defaultConfiguration.putAll(map);
            Configuration.setConfiguration(defaultConfiguration);
            logger.debug("configuration load successfully " + Configuration.getConfiguration());
        }catch(Exception e){
            logger.error("fail load listener configuration ",e);
        }
        return this;
    }

}
