package org.softauto;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.config.Context;
import org.softauto.core.Configuration;
import org.softauto.discovery.Discovery;
import org.softauto.utils.Util;


public class Discover {

    private static Logger logger = LogManager.getLogger(Discover.class);

    public Discover(String...args){
        initialize(args);
        JsonNode discovery = new Discovery().discover().getDiscovery();
        if(discovery != null ){
            if(args.length == 2) {
                Util.save(discovery,args[1]);
            }else {
                Util.save(discovery);
            }
            logger.debug("discovery finish successfully");
        }else {
            logger.error("discovery fail no items found ");
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
            initializeArgs(args);
        }catch (Exception e){
           logger.error("initialize discovery fail ",e.getMessage());
        }
    }

    private static void initializeArgs(String[] args){
        if(args.length == 2){
           Configuration.put(Context.OUTPUT_FILE_PATH, args[1].trim());
        }
    }








}
