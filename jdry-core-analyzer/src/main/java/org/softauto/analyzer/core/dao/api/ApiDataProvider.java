package org.softauto.analyzer.core.dao.api;


import org.softauto.analyzer.model.genericItem.GenericItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApiDataProvider {

    private static ApiDataProvider apiDataProvider = null;

    List<GenericItem> trees = new ArrayList<>();

    HashMap<String,String> entities = new HashMap<>();

    Parser parser;

    public static ApiDataProvider getInstance(){
        if(apiDataProvider == null){
            apiDataProvider = new ApiDataProvider();
        }
        return apiDataProvider;
    }

    private ApiDataProvider(){

    }

    public  ApiDataProvider setParser(Parser parser){
        this.parser = parser;
        return this;
    }

    public HashMap<String, String> getEntities() {
        return entities;
    }

    public List<GenericItem> getTrees() {
        return trees;
    }


    public ApiDataProvider initialize(){
        Parser parser = this.parser.parseProcess();
        trees = parser.getGenericItems();
        entities = parser.getEntities();
        return this;
    }
}
