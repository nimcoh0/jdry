package org.softauto.spring.web.analyzer.initializers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.core.system.plugin.api.Provider;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.model.test.Test;
import org.softauto.analyzer.core.system.config.Configuration;
import org.softauto.analyzer.core.utils.Analyzer;
import org.softauto.analyzer.core.utils.TreeTools;
import org.softauto.spring.web.analyzer.analyzers.AnalyzeAnnotations;
import org.softauto.spring.web.analyzer.util.CallOptionBuilder;
import org.softauto.spring.web.analyzer.handlers.HandleRequestType;

import java.util.*;

public class BaseInitializer implements Analyzer {

    private static Logger logger = LogManager.getLogger(BaseInitializer.class);

    static GenericItem tree;

    static Test test;

    Provider provider;

    public BaseInitializer setProvider(Provider provider) {
        this.provider = provider;
        return this;
    }

    public BaseInitializer setTree(GenericItem tree) {
        BaseInitializer.tree = tree;
        return this;
    }

    public  BaseInitializer setTest(Test test) {
        BaseInitializer.test = test;
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
                boolean r = updateGenericItem(tree);
                if (r) {
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
                }
            } catch (Exception e) {
                logger.error("fail initialize " + tree.getName(), e);
            }
            return callOption;
        }
        return null;
    }




    private String getRestMainClass(LinkedHashMap<String,Object> classList){
        for(Map.Entry classes : classList.entrySet()){
            for(Map.Entry annotations : ((LinkedHashMap<String,Object>)classes.getValue()).entrySet()){
                if(annotations.getKey().equals("Lorg/springframework/web/bind/annotation/RequestMapping;")){
                    return classes.getKey().toString();
                }
            }
        }
        return null;
    }

    private boolean updateGenericItem(GenericItem genericItem) {
        String mainClass = getRestMainClass(genericItem.getClassList());
        if(mainClass.equals(genericItem.getNamespce())){
            return true;
        }else if(!isDuplicateRestCall(genericItem)){
            genericItem.setNamespce(mainClass);
            genericItem.setNamespaceChange(true);
            return true;
        }
        return false;
    }

    private boolean isDuplicateRestCall(GenericItem genericItem){
        try {
            boolean start = false;
            for(Map.Entry entry : genericItem.getClassList().entrySet()) {
                if (entry.getKey().equals(genericItem.getNamespce() )) {
                    start = true;
                }
                if(start && !entry.getKey().equals(genericItem.getNamespce())){
                    List<String> list = new ArrayList<>();
                    List<String> excludeList = new ArrayList<>();
                    if (TreeTools.isDuplicateAnnotationMethod(entry.getKey().toString(), genericItem.getAnnotationsHelper(), list,excludeList)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /*
    public void updateGenericItem(GenericItem genericItem) {
        String mainClass = getRestMainClass(genericItem.getClassList());
        if(!mainClass.equals(genericItem.getNamespce())) {
            genericItem.setNamespce(mainClass);
            genericItem.setNamespaceChange(true);
        }
    }

     */

    /*
    public static <R> R postPlugin(){
        List<Test> tests = new ArrayList<>();
        //HashMap<String,Object> callOption = (HashMap<String,Object>)o;
        try {
            Data data = test.getData();
            tests.add(test);
            //test.getData().setPluginData(callOption);
            if(data.getPluginData().get("path") instanceof ArrayList<?>){
                List<String> pathList = (List<String>) data.getPluginData().get("path");
                for(int i=1;i<pathList.size();i++){
                    Test newTest = (Test) SerializationUtils.clone(test);
                    newTest.getData().getPluginData().put("path",pathList.get(i));
                    newTest.setName(test.getName());
                    newTest.setTestId(Utils.getShortName(newTest.getName()));
                    newTest.setFullName(TestNameGenerator.getInstance().generateNameIfExist(newTest.getNamespace()+"."+newTest.getName()));
                    tests.add(newTest);
                }
                test.getData().getPluginData().put("path",pathList.get(0));
                logger.debug("creating "+ pathList.size()+" new test for "+ test.getFullName());
            }
        } catch (Exception e) {
            logger.error("fail post plugin "+test.getFullName(),e);
        }
        return (R)tests;
    }


     */

}
