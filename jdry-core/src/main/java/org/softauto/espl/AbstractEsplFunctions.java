package org.softauto.espl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.softauto.core.Multimap;

import java.util.HashMap;

public class AbstractEsplFunctions {

    static Multimap _publish;

    public static void setPublish(Multimap publish) {
        _publish = publish;

    }

    public static Object getPublish(String expression){
        try {
            String root = new ObjectMapper().writeValueAsString((HashMap<String, Object>)_publish.getMap());
            JsonNode rootNode = new ObjectMapper().readTree(root);
            return rootNode.at(expression);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static String getExpressionType(String expression){
        if(expression.contains("T(")){
           return StringUtils.substringBetween(expression,"T(",")");
        }
        return "java.lang.String";
    }


}
