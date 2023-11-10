package org.softauto.analyzer;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.item.TreeScanner;
import org.softauto.analyzer.model.suite.Suite;

/**
 * analyze the discovery & recorded files and produce pre compile json
 * this analyzer performs only base and Annotation analyze
 */
public class Analyzer {

    private static Logger logger = LogManager.getLogger(Analyzer.class);

    static Suite suite;

    public static Suite getSuite() {
        return suite;
    }

    public Analyzer(String conf,String discovery,String output){
        logger.info("start analyzer");
        org.softauto.analyzer.core.Main m = new org.softauto.analyzer.core.Main(conf,discovery,output);
        build();
        m.save(suite);
        logger.info("end analyzer");
    }

    public Analyzer(String conf,String discovery,String recorder, String output){
        logger.info("start analyzer");
        org.softauto.analyzer.core.Main m = new org.softauto.analyzer.core.Main(conf,discovery,recorder,output);
        build();
        m.save(suite);
        logger.info("end analyzer");
    }

    public static void main(String[] args) {
        logger.info("start analyzer");
        org.softauto.analyzer.core.Main m = new org.softauto.analyzer.core.Main(args);
        build();
        m.save(suite);
        logger.info("end analyzer");
    }

    public static void build(){
        suite = new TreeScanner().getSuite();

    }
}
