package org.softauto.jaxrs.analyzer.initializers;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.model.data.Data;
import org.softauto.analyzer.model.test.Test;
import org.softauto.analyzer.core.utils.Analyzer;
import org.softauto.jaxrs.analyzer.PluginHelperImpl;


import java.util.HashMap;

public class DataInitializer  implements Analyzer {

    private static Logger logger = LogManager.getLogger(DataInitializer.class);

    private Test test;

    private Data data;



    public DataInitializer setTest(Test test) {
        this.test = test;
        return this;
    }

    public  DataInitializer setData(Data data) {
        this.data = data;
        return this;
    }

    public  HashMap<String,Object> initialize() {
        try {
            JsonNode request =  PluginHelperImpl.getRequest((Data) data);
            if(request != null && test.getData().getPluginData().get("path") != null) {
                if(equals(test.getData().getPluginData().get("path").toString(), PluginHelperImpl.getPath(request))){
                    logger.debug("successfully initialize plugin data "+test.getFullName());
                    /*
                    HashMap<String, Object> newtCallOption =  PluginHelperImpl.getCallOption(data);
                    ObjectMapper objectMapper = new ObjectMapper();
                    String json = objectMapper.writeValueAsString(test.getData().getPluginData());
                    String newJson = objectMapper.writeValueAsString(newtCallOption);
                    JsonNode OrgCallOption = objectMapper.readTree(json);
                    ObjectReader objectReader = objectMapper.readerForUpdating(OrgCallOption);
                    JsonNode update = objectReader.readValue(newJson);
                    String updateJson = objectMapper.writeValueAsString(update);
                    return objectMapper.readValue(updateJson,HashMap.class);


                     */
                    return new HashMap<>();
                }
            }
        } catch (Exception e) {
            logger.error("fail initialize "+test.getFullName(),e);
        }
        return null;
    }

    private  boolean equals(String testPath,String requestPath){
        try {
            String difference = StringUtils.difference(testPath,requestPath);
            if(difference.isEmpty()){
                return true;
            }
            if(testPath.contains("{")){
                String testPathVar = "{" + StringUtils.substringBetween(testPath,"{","}")+"}";
                String testPathWithOutVar =  testPath.replace(testPathVar,"");
                String requestPathWithOutDifference = requestPath.replace(difference,"");
                if(requestPathWithOutDifference.equals(testPathWithOutVar)){
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            logger.error("fail equals "+testPath +" to "+ requestPath);
        }

        return false;
    }

}
