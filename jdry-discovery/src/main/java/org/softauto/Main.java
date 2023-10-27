package org.softauto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.config.Configuration;
import org.softauto.config.Context;
import org.softauto.discovery.Discovery;
import org.softauto.utils.Util;
import java.util.List;


public class Main {

    private static Logger logger = LogManager.getLogger(Main.class);


    public static void main(String[] args){
        initialize(args);
        List<Object> items = new Discovery().discover().getItems();
        logger.debug("discovery "+items.size() + " items");
        if(items != null && items.size() > 0){
            Util.save(items);
            logger.debug("discovery finish successfully");
        }else {
            logger.error("discovery fail no items found ");
        }
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
            Util.addJarToClasspath(Configuration.get(Context.JAR_PATH).asList());
        }catch (Exception e){
           logger.error("initialize discovery fail ",e.getMessage());
        }
    }

    private static void initializeArgs(String[] args){
        if(args.length == 2){
           Configuration.put(Context.FILE_PATH, args[1].trim());
        }
    }








}
