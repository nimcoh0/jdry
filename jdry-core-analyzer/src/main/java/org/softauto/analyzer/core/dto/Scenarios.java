package org.softauto.analyzer.core.dto;

import org.softauto.analyzer.model.data.Data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Scenarios {

    HashMap<String,LinkedList<DtoData>> _scenarios = new HashMap<>();
    HashMap<String,LinkedList<Data>> scenarios = new HashMap<>();

    public HashMap<String, LinkedList<Data>> getScenarios() {
        return scenarios;
    }

    public void setScenarios(HashMap<String, LinkedList<DtoData>> scenarios) {
        this._scenarios = scenarios;
    }

    public void addScenario(String name,LinkedList<DtoData> messages) {
        this._scenarios.put(name,messages);
    }

    public Scenarios build(){
        for(Map.Entry entry : _scenarios.entrySet() ){
            LinkedList<Data> dataList =   new DataConverter().setScenarioId(entry.getKey().toString()).convertFromDto((LinkedList<DtoData>)entry.getValue());
            scenarios.put(entry.getKey().toString(),dataList);
        }
        return this;
    }
}
