package org.softauto.espl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.softauto.core.Multimap;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.HashMap;
import java.util.Map;

public class EsplFunctions {

    static SpelExpressionParser parser = new SpelExpressionParser();

    static Multimap _publish;


    public static void setPublish(Multimap publish) {
       _publish = publish;

    }

    public static class Map{

        public static String map(String expression){
            return getPublish(expression).toString();

        }
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

}
