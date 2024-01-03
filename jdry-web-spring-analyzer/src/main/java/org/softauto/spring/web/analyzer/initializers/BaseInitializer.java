package org.softauto.spring.web.analyzer.initializers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.analyzer.core.system.plugin.api.Provider;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.core.utils.Analyzer;
import org.softauto.spring.web.analyzer.analyzers.AnalyzeAnnotations;
import org.softauto.spring.web.analyzer.util.CallOptionBuilder;
import org.softauto.spring.web.analyzer.handlers.HandleRequestType;

import java.util.*;

public class BaseInitializer implements Analyzer {

    private static Logger logger = LogManager.getLogger(BaseInitializer.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    static GenericItem tree;

    Provider provider;

    public BaseInitializer setProvider(Provider provider) {
        this.provider = provider;
        return this;
    }

    public BaseInitializer setTree(GenericItem tree) {
        BaseInitializer.tree = tree;
        return this;
    }



    public  HashMap<String,Object> initialize() {
        HashMap<String,Object> callOption = new HashMap<>();
        try {
            AnalyzeAnnotations analyzeAnnotations = new AnalyzeAnnotations().setTree(tree).build();
            callOption = CallOptionBuilder.newBuilder().setConsume(analyzeAnnotations.getConsume())
                    .setProduce(analyzeAnnotations.getProduce())
                    .setTree(tree)
                    .setProvider(provider)
                    .setPath(analyzeAnnotations.getPathList())
                    .build()
                    .getCallOption();
            callOption.put("argumentsNames", tree.getArgumentsNames());
            callOption.put("protocol", "JAXRS");
            HashMap<String, Object> argumentsRequestType = HandleRequestType.setTree(tree).build().getArgumentsRequestType();
            if (argumentsRequestType.size() > 0) {
                callOption.put("argumentsRequestType", argumentsRequestType);
            }

            logger.debug(JDRY,"successfully initialize plugin data " + tree.getName());
            return callOption;
        } catch(Exception e){
            logger.error(JDRY,"fail initialize " + tree.getName(), e);
        }


        return null;
    }









}
