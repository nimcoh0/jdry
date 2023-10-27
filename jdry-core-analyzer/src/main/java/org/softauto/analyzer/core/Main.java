package org.softauto.analyzer.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.core.dao.data.Parser;
import org.softauto.analyzer.core.system.config.Context;
import org.softauto.analyzer.core.dao.api.ApiDataProvider;
import org.softauto.analyzer.core.dao.data.DataProvider;
import org.softauto.analyzer.model.suite.Suite;
import org.softauto.analyzer.core.utils.Utils;
import org.softauto.analyzer.core.system.config.Configuration;
import java.util.ArrayList;
import java.util.Arrays;


public class Main {

    private static Logger logger =  LogManager.getLogger(Main.class);

    public static Main main = null;

    //Suite suite;

    public Main(String configuration){
        init(new String[]{configuration});
    }

    public Main(String[] args){
        logger.info(" --- start analyzer ----");
        init(args);
       // build();
       // save(suite);
    }

    public static void main(String[] args) {
        main = new Main(args);
    }

    public Main init(String[] args){
        try {
            Utils.loadDefaultConfiguration();
            if(args.length >0){
                Utils.loadConfiguration(args[0]);
            }else {
                Utils.loadConfiguration(null);
            }

            registerPlugins();
            initializeArgs(args);
            Utils.addJarToClasspath(Configuration.get(Context.JAR_PATH).asList());
            initializeApi();
            initializeData();
            //HookListener.setStart(true);
            logger.debug("initialize successfully");
        }catch (Exception e){
            logger.error("fail initialize ",e);
        }
        return this;
    }

    private static void initializeArgs(String[] args){
        if(args.length == 3){
            Configuration.put(Context.FILE_INPUT_PATH, args[1].trim());
            Configuration.put(Context.FILE_OUTPUT_PATH, args[2].trim());
        }else {

        }
    }

    /*
    public Main build(){
        suite = new TreeScanner().getSuite();
        return this;
    }

     */

    public  Main save(Suite suite){
        try {
            Utils.toJson(Configuration.get(Context.FILE_OUTPUT_NAME).asString(), Configuration.get(Context.FILE_OUTPUT_PATH).asString(), suite);
            logger.debug("Suite save successfully on "+Configuration.get(Context.FILE_OUTPUT_NAME).asString(), Configuration.get(Context.FILE_OUTPUT_PATH).asString());
        } catch (Exception e) {
            logger.error("fail save Suite ",e);
        }
        return this;
    }

    private static void registerPlugins(){
        try {
            if(Configuration.has(Context.PLUGIN_JARS)) {
                Object plugins = Configuration.get(Context.PLUGIN_JARS).asList();
                if (plugins != null) {
                    for (Object s : (ArrayList) plugins) {
                        Utils.addJarToClasspath(Arrays.asList(new String[]{s.toString()}));
                        logger.debug("plugin " + s.toString() + " register save successfully");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("fail register Plugins ",e);
        }
    }

    public ApiDataProvider initializeApi(){
        org.softauto.analyzer.core.dao.api.Parser parser = new org.softauto.analyzer.core.dao.api.Parser(Configuration.get(Context.FILE_INPUT_PATH).asString()+"/"+Configuration.get(Context.FILE_INPUT_NAME).asString());
        return ApiDataProvider.getInstance().setParser(parser);
    }

    public static DataProvider initializeData(){
        Parser parser = new Parser(Configuration.get(Context.FILE_DATA_PATH).asString()+"/"+Configuration.get(Context.FILE_DATA_NAME).asString());
        return DataProvider.getInstance().setParser(parser);
    }

}
