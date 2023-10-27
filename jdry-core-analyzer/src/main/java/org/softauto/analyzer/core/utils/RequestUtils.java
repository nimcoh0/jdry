package org.softauto.analyzer.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.softauto.analyzer.core.system.config.Configuration;
import org.softauto.analyzer.directivs.argument.Argument;
import org.softauto.analyzer.directivs.request.Request;
import org.softauto.analyzer.model.genericItem.GenericItem;
import java.util.LinkedList;


public class RequestUtils {



    private static boolean entityIdentify(Argument argument){
        return  (Boolean) ApplyRule.setRule("entity_identify").addContext("argument",argument).apply().getResult();
    }

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
                    String[] words = StringUtils.splitByCharacterTypeCamelCase(o);
                    String[] newWords = Utils.fixsplitByCharacterTypeCamelCase(words);
                    o = StringUtils.join(newWords, "/").toLowerCase();
                }
                argument.addValue("(" + argument.getType() + ")retrieve(\"/" + o + "\",\"" + argument.getType() + "\");");
                //Boolean result = (Boolean) ApplyRule.setRule("entity_identify").addContext("argument",argument).apply().getResult();
                Boolean result = entityIdentify(argument);
                argument.setEntity(result);
                arguments.add(argument);
            }
            request.setArguments(arguments);
        }
        return request;
    }



}
