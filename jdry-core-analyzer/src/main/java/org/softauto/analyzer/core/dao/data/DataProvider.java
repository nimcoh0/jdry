package org.softauto.analyzer.core.dao.data;

import org.softauto.analyzer.core.dto.Scenarios;

public class DataProvider {

    private static DataProvider dataProvider = null;

    Parser parser;

    private Scenarios scenarios = new Scenarios();


    private DataProvider(){

    }

    public static DataProvider getInstance(){
        if(dataProvider == null){
            dataProvider = new DataProvider();
        }
        return dataProvider;
    }

    public DataProvider setParser(Parser parser) {
        this.parser = parser;
        return this;
    }

    public Scenarios getScenarios() {
        return scenarios;
    }

    public DataProvider initialize(){
        scenarios = parser.parseData().getScenarios();
        return this;
    }

}
