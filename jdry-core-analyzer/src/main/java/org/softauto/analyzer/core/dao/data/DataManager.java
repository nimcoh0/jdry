package org.softauto.analyzer.core.dao.data;

import org.softauto.analyzer.model.data.Data;

import java.util.LinkedList;
import java.util.Map;

public class DataManager {

    private DataProvider dataProvider;

    private Map.Entry scenario;

    private LinkedList<Data> dataList = new LinkedList<>();

    private String testName;

    private boolean isTestHasData = false;

    public boolean isTestHasData() {
        return isTestHasData;
    }

    public DataManager setTestName(String testName) {
        this.testName = testName;
        return this;
    }

    public DataManager(DataProvider dataProvider){
        this.dataProvider = dataProvider;
    }


    public Map.Entry getScenario() {
        return scenario;
    }

    public LinkedList<Data> getDataList() {
        return dataList;
    }


    public DataManager initializeData(String name ){
        dataList = new LinkedList<>();
        for(Map.Entry scenario :  dataProvider.getScenarios().getScenarios().entrySet()) {
            LinkedList<Data> _dataList = ((LinkedList<Data>)scenario.getValue());
            for(Data data : _dataList){
                if((data.getClazz()+"."+data.getMethod()).equals(name)){
                    dataList.add(data);
                }
            }
        }
        return this;
    }

    public DataManager getAllData(){
        dataList = new LinkedList<>();
        for(Map.Entry scenario :  dataProvider.getScenarios().getScenarios().entrySet()) {
            LinkedList<Data> _dataList = ((LinkedList<Data>)scenario.getValue());
            for(Data data : _dataList){
                //if((data.getClazz()+"."+data.getMethod()).equals(name)){
                    dataList.add(data);
               // }
            }
        }
        return this;
    }

    public LinkedList<Data> getDataByPlugin(String plugin){
        LinkedList<Data> _dataList = new LinkedList<>();
        if(dataList != null) {
            for (Data data : dataList) {
                if (data.getPlugin() != null && data.getPlugin().equals(plugin)) {
                    _dataList.add(data);
                }
            }
        }
        return _dataList;
    }

    public LinkedList<Data> getdDataExcludePlugin(){
        LinkedList<Data> _dataList = new LinkedList<>();
        for(Data data : dataList){
            if(data.getPlugin() == null){
               _dataList.add(data);
            }
        }
        return _dataList;
    }


    public LinkedList<Data> getdDatabyFqmn(String fqmn){
        LinkedList<Data> _dataList = new LinkedList<>();
        for(Data data : this.dataList){
            String f = data.getClazz()+"."+data.getMethod();
            if(f.equals(fqmn)){
                _dataList.add(data);
            }
        }
        return _dataList;
    }


    public LinkedList<Data> getdDatabyFqmnExcludePlugin(String fqmn){
        LinkedList<Data> _dataList = new LinkedList<>();
        if(dataList != null && dataList.size() > 0 ) {
            for (Data data : dataList) {
                String f = data.getClazz() + "." + data.getMethod();
                if (f.equals(fqmn) && data.getPlugin() == null) {
                    _dataList.add(data);
                }
            }
        }
        return _dataList;
    }





}
