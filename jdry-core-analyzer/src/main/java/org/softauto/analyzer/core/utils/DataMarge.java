package org.softauto.analyzer.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.softauto.analyzer.directivs.argument.Argument;
import org.softauto.analyzer.directivs.request.Request;
import org.softauto.analyzer.directivs.result.Result;
import org.softauto.analyzer.model.data.Data;

import java.util.HashMap;
import java.util.Map;

public class DataMarge {

    Data testData;

    Data recordedData;

    ObjectMapper objectMapper = new ObjectMapper();

    public DataMarge(){
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    public DataMarge setTestData(Data testData) {
        this.testData = testData;
        return this;
    }

    public DataMarge setRecordedData(Data recordedData) {
        this.recordedData = recordedData;
        return this;
    }

    private int margeId(){
        if(recordedData.getId() > -1){
            return recordedData.getId();
        }
        return testData.getId();
    }

    private String margePlugin(){
        if(recordedData.getPlugin() != null && !recordedData.getPlugin().isEmpty()){
            return recordedData.getPlugin();
        }
        return testData.getPlugin();
    }

    private String margeClazz(){
        if(recordedData.getClazz() != null && !recordedData.getClazz().isEmpty()){
            return recordedData.getClazz();
        }
        return testData.getClazz();
    }

    private String margeMethod(){
        if(recordedData.getMethod() != null && !recordedData.getMethod().isEmpty()){
            return recordedData.getMethod();
        }
        return testData.getMethod();
    }

    private long margeThread(){
        if(recordedData.getThread() != -1 ){
            return recordedData.getThread();
        }
        return testData.getThread();
    }

    private long margeTime(){
        if(recordedData.getTime() != -1 ){
            return recordedData.getTime();
        }
        return testData.getTime();
    }

    private String margeScenarioId(){
        if(recordedData.getScenarioId() != null && !recordedData.getScenarioId().isEmpty()){
            return recordedData.getScenarioId();
        }
        return testData.getScenarioId();
    }

    private boolean margeUsed(){
            return recordedData.isUsed();
    }



    private Map<String, Object> margePluginData(){
        Map<String, Object> hm = testData.getPluginData();
        for(Map.Entry entry : recordedData.getPluginData().entrySet()){
            if(hm.containsKey(entry)){
                if(entry.getValue() != null){
                    hm.put(entry.getKey().toString(),entry.getValue());
                }
            }else {
                hm.put(entry.getKey().toString(),entry.getValue());
            }
        }
        return hm;
    }

    private Request margeRequest(){
       Request testRequest =  testData.getRequest();
       Request recordedRequest =  recordedData.getRequest();
       for(int i=0;i<recordedRequest.getArguments().size();i++){
           Argument recordedArgument =  recordedRequest.getArguments().get(i);
           Map<String, Object> recordedMap = objectMapper.convertValue(recordedArgument, new TypeReference<Map<String, Object>>() {});
           Map<String, Object> testdMap = objectMapper.convertValue(testRequest.getArguments().get(i), new TypeReference<Map<String, Object>>() {});
           for(Map.Entry entry : recordedMap.entrySet()){
                   testdMap.put(entry.getKey().toString(), entry.getValue());
           }
           testdMap.remove("values");

           //testdMap.putAll(recordedMap);
           Argument argument = objectMapper.convertValue(testdMap, Argument.class);
           testRequest.getArguments().set(i,argument);
       }
        return testRequest;
    }

    private Result margeResponse(){
        Result testResult =  testData.getResponse();
        Result recordedResult =  recordedData.getResponse();
        Argument recordedArgument =  recordedResult;
        Map<String, Object> recordedMap = objectMapper.convertValue(recordedResult, new TypeReference<Map<String, Object>>() {});
        Map<String, Object> testdMap = objectMapper.convertValue(testResult, new TypeReference<Map<String, Object>>() {});
        for(Map.Entry entry : recordedMap.entrySet()){
            testdMap.put(entry.getKey().toString(), entry.getValue());
        }
        testdMap.remove("values");
        //testdMap.putAll(recordedMap);
        Result result = objectMapper.convertValue(testdMap, Result.class);
        return result;
    }


    public Data marge(){
        Data margeData = new Data();
        margeData.setId(margeId());
        margeData.setClazz(margeClazz());
        margeData.setPlugin(margePlugin());
        margeData.setMethod(margeMethod());
        margeData.setThread(margeThread());
        margeData.setTime(margeTime());
        margeData.setScenarioId(margeScenarioId());
        margeData.setUsed(margeUsed());
        margeData.setPluginData((HashMap<String, Object>) margePluginData());
        margeData.setRequest(margeRequest());
        margeData.setResponse(margeResponse());
        return margeData;
    }
}
