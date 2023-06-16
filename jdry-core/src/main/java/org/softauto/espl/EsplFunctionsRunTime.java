package org.softauto.espl;

import org.apache.commons.lang3.StringUtils;
import org.softauto.core.BracketsUtils;
import org.softauto.core.Utils;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class EsplFunctionsRunTime extends AbstractEsplFunctions{

    //static SpelExpressionParser parser = new SpelExpressionParser();

    public static class Map{


        public static String map(String expression){
            String str = "map(\"" + expression + "\")";
            String type = getExpressionType(expression);
            if(type != null && Utils.isPrimitive(type)) {
                return Utils.toObject(type, str);
            }else if(type != null ) {
                return "("+type+")"+ str;
            }
            return null;
        }
    }

    public static class Strategy{

        public static Object strategy(String type){
            try {
                Class c = Class.forName(type,false,ClassLoader.getSystemClassLoader());
                return c.getConstructors()[0].newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        public static void exclude(String property){
            List<String> excludedFields = (List<String>) Espl.getInstance().getProperty("excludedFields");
            excludedFields.add(property);
        }

        public static String randomk(String type,String...attributes){
            String attributeString = "";
            if(attributes != null && attributes.length > 0) {
                for (String s : attributes) {
                    if(attributeString.isEmpty()) {
                        attributeString ="\"" + s + "\"";
                    }else {
                        attributeString = attributeString + ",\"" + s + "\"";
                    }
                }
            }
           return Utils.toObject(type,"randomk(\""+type+"\","+attributeString+")");
        }

        public static String random(String type){
          //String type = Espl.getInstance().getProperty("type").toString();
          return Utils.toObject(type,"random(\""+type+"\")");

        }
    }

    public static class Consume{


        protected static String removeType(String expression){
            if(expression.contains("T(")){
                return  expression.substring(expression.lastIndexOf("(")+1,expression.length()-1);
                //return StringUtils.substringAfter(expression,")");
            }
            return expression;
        }

        private static boolean hasLeftSide(String expression){
            if(expression.startsWith("#consume") ){
                return false;
            }
            return true;
        }

        private static boolean hasRightSide(String expression,String content){
            BracketsUtils bracketsUtils = new BracketsUtils().setExpression(expression).setOpenBracketTag("(").setCloseBracketTag(")");
            LinkedHashMap<Integer, Integer> groups =  bracketsUtils.analyze().getGroups();
            AtomicReference<Boolean> ref = new AtomicReference<>(false);
            groups.forEach((k,v)->{
                java.util.Map.Entry entry = bracketsUtils.getGroup(k,v);
                String txt = bracketsUtils.getGroupLiteralText(entry);
                if(txt.contains(content)){
                    if(expression.length() > v){
                        ref.set(true);
                    }
                }
            });

            return ref.get();
        }

        public static String consume(String type,String expression){
            Espl espl = Espl.getInstance();
            boolean hasLeft = false ;
            boolean hasRight = false;
            String str = "consume(\""  + expression +"\")";
            if(espl.getPropertyRunTime("expression") != null) {
                String exp = espl.getPropertyRunTime("expression").toString();
                hasLeft = hasLeftSide(exp);
                hasRight = hasRightSide(exp,"'"+type+"','"+expression+"'");
            }
            str = Utils.toObject(type, str.replace("'", "\""));
            if(type != null) {
                if (hasLeft ) {
                    str = "\"+" + str;
                }
                if(hasRight) {
                    str = str + "+\"";
                }
            }

            return str;
        }
    }

    /*
    public static class Step {

        static List<org.softauto.analyzer.model.step.Step> _steps;

        public static Class setSteps(List<org.softauto.analyzer.model.step.Step> steps) {
            _steps = steps;
            return Step.class;
        }

        private static org.softauto.analyzer.model.step.Step findStepById(String id) {
            id = id.substring(1,id.length());
            if(_steps != null && _steps.size() > 0) {
                for (org.softauto.analyzer.model.step.Step step : _steps) {
                    if (step.getId() != null && step.getId().equals(id)) {
                        return step;
                    }
                }
            }
            return null;
        }

        private static org.softauto.analyzer.model.step.Step findStepByName(String name) {
            if(_steps != null && _steps.size() > 0) {
                for (org.softauto.analyzer.model.step.Step step : _steps) {
                    if (step.getMethod().equals(name)) {
                        return step;
                    }
                }
            }
            return null;
        }



        public static String createStep(String method,String args) {
            try {
                org.softauto.analyzer.model.step.Step step = null;
                if(method.startsWith("#")){
                    step = findStepById(method);
                }else {
                    step = findStepByName(method);
                }
                if (step != null)
                    return StepCallBuilder.newBuilder().setStep(step).setArgs(args).build().getExpression();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

     */

}
