package org.softauto.analyzer.core.dao.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.core.dto.DtoData;
import org.softauto.analyzer.core.dto.Scenarios;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class Parser {

    private static Logger logger = LogManager.getLogger(Parser.class);

    JsonNode json;

    ObjectMapper objectMapper;

    Scenarios scenarios = new Scenarios();

    public Scenarios getScenarios() {
        return scenarios.build();
    }

    public Parser(String path){
        try {
            objectMapper = new ObjectMapper();
            if(Paths.get(path).toFile().exists()) {
                json = objectMapper.readValue(Paths.get(path).toFile(), JsonNode.class);
            }
            logger.debug("successfully parse "+path);
        } catch (IOException e) {
            logger.error("parser fail for ",path,e);
        }
    }

    private List<String> getScenariosNames(JsonNode json){
        List<String> names = new ArrayList<>();
        try {
            String str = new ObjectMapper().writeValueAsString(json);
            Map<String, Object> jsonElements = new ObjectMapper().readValue(str, new TypeReference<Map<String, Object>>() {});
            for(Map.Entry entry : jsonElements.entrySet()){
                names.add(entry.getKey().toString());
                logger.debug("successfully got scenario for "+entry.getKey().toString());
            }

        } catch (JsonProcessingException e) {
            logger.error("fail getting scenario",e );
        }
        return names;
    }


    public Parser parseData(){
        try {
            if(json != null ){
                List<String> names = getScenariosNames(json);
                int counter =0;
                for(JsonNode scenario : json){
                    LinkedList<DtoData> dataList = new LinkedList<>();
                    for(JsonNode node : scenario) {
                        String jsonString = objectMapper.writeValueAsString(node);
                        DtoData dtoData = objectMapper.readValue(jsonString, DtoData.class);
                        dataList.add(dtoData);
                    }
                   logger.debug("number of data to process for scenario "+ names.get(counter) +" "+ dataList.size());
                   scenarios.addScenario(names.get(counter),dataList) ;
                   counter++;
                }
            }
            logger.debug("number of scenarios "+ scenarios.getScenarios().size());
            logger.debug("successfully parse data");
        } catch (JsonProcessingException e) {
            logger.error("data parsing fail ",e);
        }
        return this;
    }

}
