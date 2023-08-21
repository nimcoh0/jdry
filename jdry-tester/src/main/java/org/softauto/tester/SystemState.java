package org.softauto.tester;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.softauto.core.*;
import org.softauto.core.DefaultConfiguration;
import org.softauto.listener.ListenerObserver;
import org.softauto.listener.ListenerServerProviderImpl;
import org.yaml.snakeyaml.Yaml;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;



public class SystemState {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(SystemState.class);

    private static SystemState systemState = null;

    private SystemState(){};
    Yaml yaml = new Yaml();
    static ObjectMapper objectMapper;

    public static SystemState getInstance(){
        if(systemState == null){
            systemState = new SystemState();
            objectMapper = new ObjectMapper(new YAMLFactory());
            loadDefaultConfiguration();
        }
        return systemState;
    }


    public void initialize() throws IOException {
        loadConfiguration();
        if(sayHello()){
               logger.debug("successfully say hello");
               if(sendConfiguration()) {
                   logger.debug("successfully send configuration");
               }else {
                   logger.error("fail send configuration ");
               }
           }else {
               logger.error("fail say hello ");
        }
    }


    private Boolean sayHello(){
        try {
            return new InvocationHandler().invoke("org_softauto_system_SystemServiceImpl_hello", new Object[]{}, new Class[]{});

         }catch (Exception e){
            logger.error("fail sayHello",e);
        }
        return null;
    }

    public Boolean sendConfiguration(){
        try {
            org.softauto.core.Context.setTestState(TestLifeCycle.INITIALIZE);
            return new InvocationHandler().invoke("org_softauto_system_SystemServiceImpl_configuration", new Object[]{Configuration.getConfiguration()}, new Class[]{HashMap.class});
        }catch (Exception e){
            logger.error("fail send configuration",e);
        }
        return null;
    }

    public Boolean shutdown() throws Exception{
        return new InvocationHandler().invoke("org_softauto_system_SystemServiceImpl_shutdown", new Object[]{}, new Class[]{});
    }

    public Boolean startTest(String testname)throws Exception{
        ListenerObserver.getInstance().reset();
        boolean r = new InvocationHandler().invoke("org_softauto_system_SystemServiceImpl_startTest", new Object[]{testname}, new Class[]{String.class});
        org.softauto.core.Context.setTestState(TestLifeCycle.START);
        return r;
    }

    public Boolean endTest(String testname)throws Exception{
        boolean r = new InvocationHandler().invoke("org_softauto_system_SystemServiceImpl_endTest", new Object[]{testname}, new Class[]{String.class});
        org.softauto.core.Context.setTestState(TestLifeCycle.STOP);
        ListenerServerProviderImpl.getInstance().shutdown();
        return r;
    }

    public static void loadDefaultConfiguration()  {
        try {
               Configuration.setConfiguration(DefaultConfiguration.getConfiguration());
            logger.debug("default configuration load successfully " + Configuration.getConfiguration());
        }catch(Exception e){
            logger.error("fail load default configuration ",e);
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
            logger.debug("configuration load successfully " + Configuration.getConfiguration());
        }catch(Exception e){
            logger.error("fail load listener configuration ",e);
        }
        return this;
    }


}
