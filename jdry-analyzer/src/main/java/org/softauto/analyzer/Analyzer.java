package org.softauto.analyzer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.analyzer.core.system.config.Context;
import org.softauto.analyzer.core.dao.api.ApiDataProvider;
import org.softauto.core.Analyze;
import org.softauto.analyzer.core.utils.Utils;
import org.softauto.analyzer.core.system.config.Configuration;
import org.softauto.analyzer.item.TreeScanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class Analyzer {

    private static Logger logger =  LogManager.getLogger(Analyzer.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    public static Analyzer analyzer = null;

    public Analyzer(String configuration){
        init(configuration);
        Analyze analyze = new TreeScanner().getAnalyze();
        save(analyze);
    }

    public Analyzer(String[] args){
        logger.info(JDRY," --- start analyzer ----");
        init(args[0]);
        initializeArgs(args);
        initializeApi();
        Analyze analyze = new TreeScanner().getAnalyze();
        save(analyze);

    }

    public Analyzer(String conf, String discovery, String output){
        logger.info(JDRY," --- start analyzer ----");
        init(conf);
        Configuration.put(Context.DISCOVERY_INPUT_FILE, discovery);
        Configuration.put(Context.FILE_OUTPUT,output);
        initializeApi();
        Analyze analyze = new TreeScanner().getAnalyze();
        save(analyze);

    }

    public Analyzer(String conf, String discovery, String recorder, String output){
        logger.info(JDRY," --- start analyzer ----");
        init(conf);
        Configuration.put(Context.DISCOVERY_INPUT_FILE, discovery);
        Configuration.put(Context.FILE_OUTPUT,output);
        initializeApi();
        Analyze analyze = new TreeScanner().getAnalyze();
        save(analyze);
    }

    public static void main(String[] args) {
        analyzer = new Analyzer(args);
    }

    public Analyzer init(String conf){
        try {
            Utils.loadDefaultConfiguration();
            if(conf != null){
                Utils.loadConfiguration(conf);
            }else {
                Utils.loadConfiguration(null);
            }

            registerPlugins();
            Utils.addJarToClasspath(Configuration.get(Context.JAR_PATH).asList());
            logger.debug(JDRY,"initialize successfully");
        }catch (Exception e){
            logger.error(JDRY,"fail initialize ",e);
        }
        return this;
    }

    private static void initializeArgs(String[] args){
        if(args.length == 3){
            Configuration.put(Context.DISCOVERY_INPUT_FILE, args[1].trim());
            Configuration.put(Context.FILE_OUTPUT,args[2].trim());
        }else {

        }
    }


    public Analyzer save(Analyze analyze){
        try {
            Utils.save(analyze.toJson(), Configuration.get(Context.FILE_OUTPUT).asString() );
            logger.debug(JDRY,"Suite save successfully on "+Configuration.get(Context.FILE_OUTPUT).asString());
        } catch (Exception e) {
            logger.error(JDRY,"fail save Suite ",e);
        }
        return this;
    }


    private static void registerPlugins(){
        try {
            if(Configuration.has(Context.PLUGIN_JARS)) {
                Object plugins = Configuration.get(Context.PLUGIN_JARS).asList();
                if (plugins != null) {
                    for (Object s : (ArrayList) plugins) {
                        Utils.addJarToClasspath(Arrays.asList(new String[]{((HashMap)s).get("jar").toString()}));
                        logger.debug(JDRY,"plugin " + s.toString() + " register save successfully");
                    }
                }
            }
        } catch (Exception e) {
            logger.error(JDRY,"fail register Plugins ",e);
        }
    }

    public ApiDataProvider initializeApi(){
        org.softauto.analyzer.core.dao.api.Parser parser = new org.softauto.analyzer.core.dao.api.Parser(Configuration.get(Context.DISCOVERY_INPUT_FILE).asString());
        return ApiDataProvider.getInstance().setParser(parser);
    }



}
