package org.softauto;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.config.Configuration;
import org.softauto.config.Context;
import org.softauto.core.api.ApiDataProvider;
import org.softauto.spark.Discovery;
import org.softauto.utils.*;
import org.softauto.utils.Util;
import java.util.ArrayList;
import java.util.List;


public class Discover {

    private static Logger logger = LogManager.getLogger(Discover.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    private List<String> discoveryAnnotations = new ArrayList<>();

    public Discover(String...args){
        initialize(args);
        JsonNode discovery = new Discovery().discover().getDiscovery();
        if(discovery != null ){
            if(args.length == 2) {
                Util.save(discovery,args[1]);
            }else {
                Util.save(discovery);
            }
            logger.debug(JDRY,"discovery finish successfully");
        }else {
            logger.error(JDRY,"discovery fail no items found ");
        }
    }

    public static void main(String[] args){
        new Discover(args);
    }

    public static void initialize(String[] args) {
        try {
            Util.loadDefaultConfiguration();
            if(args.length >0){
                Util.loadConfiguration(args[0]);
            }else {
                Util.loadConfiguration(null);
            }
            //Util.addJarToClasspath(Configuration.get(Context.JAR_PATH).asList());
            initializeArgs(args);

        }catch (Exception e){
           logger.error(JDRY,"initialize discovery fail ",e.getMessage());
        }
    }

    private static void initializeArgs(String[] args){
        if(args.length == 3){
           Configuration.put(Context.DISCOVERY_INPUT_FILE, args[1].trim());
           Configuration.put(Context.OUTPUT_FILE_PATH, args[2].trim());
        }
    }


    public Discover init(String conf){
        try {
            Util.loadDefaultConfiguration();
            if(conf != null){
                Util.loadConfiguration(conf);
            }else {
                Util.loadConfiguration(null);
            }
            logger.debug(JDRY,"initialize successfully");
        }catch (Exception e){
            logger.error(JDRY,"fail initialize ",e);
        }
        return this;
    }






    public ApiDataProvider initializeApi(){
        org.softauto.core.api.Parser parser = new org.softauto.core.api.Parser(Configuration.get(Context.DISCOVERY_INPUT_FILE).asString());
        return ApiDataProvider.getInstance().setParser(parser);
    }



}
