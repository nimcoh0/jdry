package org.softauto.analyzer.core.rules;

import org.softauto.analyzer.directivs.argument.Argument;
import org.softauto.analyzer.directivs.request.Request;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.core.utils.ApplyRule;

import java.util.LinkedList;

public class RequestRules {

    public static Request getRequest(GenericItem genericItem){
        LinkedList<Argument> arguments = new LinkedList<>();
        Request request = new Request();
        if(genericItem.getArgumentsNames() != null && genericItem.getArgumentsNames().size() > 0) {
            try {
                for (int i = 0; i < genericItem.getArgumentsNames().size(); i++) {
                    Argument argument = buildArgument(genericItem.getParametersTypes().get(i),genericItem.getArgumentsNames().get(i));
                    arguments.add(argument);
                }
            } catch (Exception e) {
                e.printStackTrace();
               // logger.error("fail build request ", e);
            }
            request.setArguments(arguments);
        }
        return request;
    }

    public static Request getDataRequest(GenericItem genericItem){
        LinkedList<Argument> arguments = new LinkedList<>();
        Request request = new Request();
        if(genericItem.getArgumentsNames() != null && genericItem.getArgumentsNames().size() > 0) {
            try {
                for (int i = 0; i < genericItem.getArgumentsNames().size(); i++) {
                    Argument argument = buildDataArgument(genericItem.getParametersTypes().get(i),genericItem.getArgumentsNames().get(i));
                    arguments.add(argument);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // logger.error("fail build request ", e);
            }
            request.setArguments(arguments);
        }
        return request;
    }

    public static Argument buildArgument(String parametersType, String parameterName){
        Argument argument = new Argument();
        argument.setType(getArgumentType(parametersType));
        argument.setName(getArgumentName(parameterName));
        argument.setContext(getArgumentContext(argument).toLowerCase());
        argument.setEntity(EntityRules.isEntity(parametersType));
        return argument;
    }

    public static Argument buildDataArgument(String parametersType, String parameterName){
        Argument argument = new Argument();
        argument.setType(getArgumentType(parametersType));
        argument.setName(getArgumentName(parameterName));
        argument.setContext(getArgumentContext(argument).toLowerCase());
        String o = (NameRules.splitByCharacterTypeCamel(argument.getName()));
        argument.setEntity(EntityRules.isEntity(parametersType));
        argument.addValue("(" + argument.getType() + ")retrieve(\"/" + getArgumentConsumeName(o) + "\",\"" + argument.getType() + "\");");
        return argument;
    }

    public static String getArgumentType(String parametersType){
        return ApplyRule.setRule("argument_type").addContext("type",parametersType).apply().getResult();
        //return parametersType.replace("$", ".");
    }

    public static String getArgumentName(String argumentsName){
        return ApplyRule.setRule("argument_name").addContext("name",argumentsName).apply().getResult();
       // return getArgumentName(argumentsName,false);
    }

    public static String getArgumentConsumeName(String argumentsName){
        return ApplyRule.setRule("argument_consume_name").addContext("name",argumentsName).apply().getResult();
        // return getArgumentName(argumentsName,false);
    }

    public static String getArgumentContext(Argument argument){
        return ApplyRule.setRule("argument_context").addContext("argument",argument).addContext("suite",org.softauto.analyzer.model.suite.Suite.class).apply().getResult();
        // return getArgumentName(argumentsName,false);
    }

    /*
    public static String getArgumentName(String argumentsName,boolean splitByCharacterTypeCamel){
        argumentsName = EntityRules.removeEntityPostfix(argumentsName);
        argumentsName = EntityRules.removeEntityPrefix(argumentsName);
        if(splitByCharacterTypeCamel){
            argumentsName = NameRules.splitByCharacterTypeCamel(argumentsName);
        }
         return argumentsName;
    }


     */
    /*
    public static Request buildRequest(GenericItem genericItem){
        String entityPostfix = Configuration.has("entity_name_postfix")? Configuration.get("entity_name_postfix").asString() : null;
        String entityPrefix = Configuration.has("entity_name_prefix")? Configuration.get("entity_name_prefix").asString() : null;
        Request request = new Request();
        if(genericItem.getArgumentsNames() != null && genericItem.getArgumentsNames().size()>0) {
            LinkedList<Argument> arguments = new LinkedList<>();
            for (int i = 0; i < genericItem.getArgumentsNames().size(); i++) {
                Argument argument = new Argument();
                argument.setType(genericItem.getParametersTypes().get(i).replace("$", "."));
                argument.setName(genericItem.getArgumentsNames().get(i));
                String o = argument.getName();
                if (entityPrefix != null && o.startsWith(entityPrefix)) {
                    o = o.replace(entityPrefix, "");
                }
                if (entityPostfix != null && o.endsWith(entityPostfix)) {
                    o = o.replace(entityPostfix, "");
                }
                if(getConfigurationParameter.param("split_publish_by_character_type_camel_case").asBoolen()){
                    //String[] words = StringUtils.splitByCharacterTypeCamelCase(o);
                    String[] newWords = StringUtilsWrapper.splitByCharacterTypeCamelCase(o);
                    o = StringUtils.join(newWords, "/").toLowerCase();
                }
                argument.setValue("(" + argument.getType() + ")retrieve(\"/" + o + "\",\"" + argument.getType() + "\");");
                //Boolean result = (Boolean) ApplyRule.setRule("entity_identify").addContext("argument",argument).apply().getResult();
                Boolean result = entityIdentify(argument);
                argument.setEntity(result);
                arguments.add(argument);
            }
            request.setArguments(arguments);
        }
        return request;
    }


     */

}
