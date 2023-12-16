package org.softauto.analyzer.core.dao.api;


import org.softauto.analyzer.model.genericItem.GenericItem;

import java.util.List;

public class ApiDataManager {

    private static ApiDataProvider apiDataProvider = null;

    public ApiDataManager(ApiDataProvider _apiDataProvider){
        this.apiDataProvider = _apiDataProvider;
    }

    public static List<GenericItem> getTrees(){
        return apiDataProvider.getTrees();
    }

    public GenericItem getTreeByFqmn(String fqmn){
        for(GenericItem item : apiDataProvider.getTrees()){
            if((item.getNamespce()+"."+item.getName()).equals(fqmn)){
                return item;
            }
        }
        return null;
    }



}
