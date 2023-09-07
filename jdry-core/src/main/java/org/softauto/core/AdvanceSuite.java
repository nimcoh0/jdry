package org.softauto.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.apache.commons.lang3.StringUtils;

public class AdvanceSuite extends AbstractSuite{

    @Override
    public <T> T getPublishParameter(String expression){

        String[] words = StringUtils.splitByCharacterTypeCamelCase(expression);

        Object o =  findSimple(expression);
        if(o != null && !o.toString().isEmpty()){
            return (T)o;
        }

        o =  findParameterByCharacterTypeCamelCase(expression);
        if(o != null && !o.toString().isEmpty()){
            return (T)o;
        }

        o =  findParameterByEntityName(expression);
        if(o != null && !o.toString().isEmpty()){
            return (T)o;
        }

        /*
        o =  findParameterByEntityName2(expression);
        if(o != null && !o.toString().isEmpty()){
            return (T)o;
        }

         */

        return null;
    }


    @Override
    public <T> T getPublishParameter(String expression,String type){
        String[] words = StringUtils.splitByCharacterTypeCamelCase(expression);

        Object o =  findSimple(expression,type);
        if(o != null && !o.toString().isEmpty() ){
            return (T)o;
        }

        o =  findParameterByCharacterTypeCamelCase(type,expression);
        if(o != null && !o.toString().isEmpty()){
            return (T)o;
        }

        o =  findParameterByEntityName(type,expression);
        if(o != null && !o.toString().isEmpty()){
            return (T)o;
        }

        /*
        o =  findParameterByEntityName2(type,expression);
        if(o != null && !o.toString().isEmpty()){
            return (T)o;
        }


         */
        return null;
    }

    /*
    private <T> T findParameterByEntityName2(String expression){
        String[] words = StringUtils.splitByCharacterTypeCamelCase(expression);
        words[1] = "/" + Configuration.get(Context.ENTITY_NAME_PREFIX).asString() + words[1] + Configuration.get(Context.ENTITY_NAME_POSTFIX).asString();
        String path = StringUtils.join(words, "/",1,words.length);
        Object o = getPublish((path+"/"+expression).toLowerCase());
        if(o != null && !o.toString().isEmpty()){
            return (T)o;
        }
        return null;
    }

    private <T> T findParameterByEntityName2(String type,String expression){
        String[] words = StringUtils.splitByCharacterTypeCamelCase(expression);
        words[1] = "/" + Configuration.get(Context.ENTITY_NAME_PREFIX).asString() + words[1] + Configuration.get(Context.ENTITY_NAME_POSTFIX).asString();
        String path = StringUtils.join(words, "/",1,words.length);
        Object o = getPublish((path+"/"+expression).toLowerCase(),type);
        if(o != null && !o.toString().isEmpty()){
            return (T)o;
        }
        return null;
    }


     */
    private <T> T findParameterByEntityName(String expression){
        String[] words = StringUtils.splitByCharacterTypeCamelCase(expression);
        words[1] = "/" + Configuration.get(Context.ENTITY_NAME_PREFIX).asString() + words[1] + Configuration.get(Context.ENTITY_NAME_POSTFIX).asString();
        String path = StringUtils.join(words, "/",1,words.length);
        Object o = getPublish(path.toLowerCase());
        if(o != null && !o.toString().isEmpty()){
            return (T)o;
        }
        return null;
    }

    private <T> T findParameterByEntityName(String type,String expression){
        String[] words = StringUtils.splitByCharacterTypeCamelCase(expression);
        words[1] = "/" + Configuration.get(Context.ENTITY_NAME_PREFIX).asString() + words[1] + Configuration.get(Context.ENTITY_NAME_POSTFIX).asString();
        String path = StringUtils.join(words, "/",1,words.length);
        Object o = getPublish(path.toLowerCase(),type);
        if(o != null && !o.toString().isEmpty()){
            return (T)o;
        }
        return null;
    }


    private <T> T findParameterByCharacterTypeCamelCase(String expression){
        String[] words = StringUtils.splitByCharacterTypeCamelCase(expression);
        String path = "/" + StringUtils.join(words, "/",1,words.length);
        Object o = getPublish(path.toLowerCase());
        if(o != null && !o.toString().isEmpty()){
            return (T)o;
        }
        return null;
    }

    private <T> T findParameterByCharacterTypeCamelCase(String type,String expression){
        String[] words = StringUtils.splitByCharacterTypeCamelCase(expression);
        String path = "/" + StringUtils.join(words, "/",1,words.length);
        Object o = getPublish(path.toLowerCase(),type);
        if(o != null && !o.toString().isEmpty()){
            return (T)o;
        }
        return null;
    }

    private <T> T findSimple(String expression){
        Object o = getPublish(expression.toLowerCase());
        if(o != null && !o.toString().isEmpty()){
            return (T)o;
        }
        return null;
    }

    private <T> T findSimple(String expression,String type){
        Object o = getPublish(expression.toLowerCase(),type);
        if(o != null && !o.toString().isEmpty()){
            return (T)o;
        }
        return null;
    }
}
