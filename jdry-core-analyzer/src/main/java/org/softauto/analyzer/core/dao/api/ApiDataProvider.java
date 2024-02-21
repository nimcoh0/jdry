package org.softauto.analyzer.core.dao.api;


import org.softauto.analyzer.model.genericItem.GenericItem;
import java.util.ArrayList;
import java.util.List;

public class ApiDataProvider {

    private static ApiDataProvider apiDataProvider = null;

    List<GenericItem> trees = new ArrayList<>();

    List<GenericItem> entities = new ArrayList<>();

    Parser parser;

    public static ApiDataProvider getInstance(){
        if(apiDataProvider == null){
            apiDataProvider = new ApiDataProvider();
        }
        return apiDataProvider;
    }

    private ApiDataProvider(){

    }

    public Parser getParser() {
        return parser;
    }

    public  ApiDataProvider setParser(Parser parser){
        this.parser = parser;
        return this;
    }

    public List<GenericItem> getTrees() {
        return trees;
    }

    public List<GenericItem> getEntities() {
        return entities;
    }

    public ApiDataProvider initialize(){
        Parser parser = this.parser.parseProcess();
        trees = parser.getGenericItems();
        entities = parser.getEntities();
        return this;
    }
}
