package org.softauto.spring.web.analyzer.initializers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.core.system.plugin.api.Provider;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.core.system.config.Configuration;
import org.softauto.analyzer.core.utils.Analyzer;
import org.softauto.spring.web.analyzer.analyzers.AnalyzeAnnotations;
import org.softauto.spring.web.analyzer.handlers.HandleRequestType;
import org.softauto.spring.web.analyzer.util.CallOptionBuilder;

import java.util.HashMap;
import java.util.List;

public class NoneTestInitializer implements Analyzer {

    private static Logger logger = LogManager.getLogger(NoneTestInitializer.class);

    static GenericItem tree;

    private Provider provider;

    public NoneTestInitializer setProvider(Provider provider) {
        this.provider = provider;
        return this;
    }

    public  NoneTestInitializer setTree(GenericItem tree) {
        NoneTestInitializer.tree = tree;
        return this;
    }

    private boolean isEndPoint(){
        if(Configuration.has("jax_rs_end_point")){
            List<String> endPointsList =   Configuration.get("jax_rs_end_point").asList();
            for(String s : endPointsList ){
                if(tree.getAnnotations().toString().contains(s)){
                    return true;
                }
            }
        }
        return false;
    }

    public  HashMap<String,Object> initialize() {
        HashMap<String,Object> callOption = new HashMap<>();
        if(isEndPoint()) {
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
                HashMap<String, Object> argumentsRequestType = HandleRequestType.setTree(tree).setCallOption(callOption).build().getArgumentsRequestType();
                if (argumentsRequestType.size() > 0) {
                    callOption.put("argumentsRequestType", argumentsRequestType);
                }

                logger.debug("successfully initialize plugin data " + tree.getName());
            } catch (Exception e) {
                logger.error("fail initialize " + tree.getName(), e);
            }
            return callOption;
        }
        return null;
    }




}
