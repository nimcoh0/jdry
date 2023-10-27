package org.softauto.analyzer.core.rules;

import org.apache.commons.lang3.StringUtils;
import org.softauto.analyzer.directivs.argument.Argument;
import org.softauto.analyzer.core.system.config.Configuration;
import org.softauto.analyzer.core.system.nlp.NameGenerator;
import org.softauto.analyzer.core.utils.ApplyRule;
import org.softauto.analyzer.core.utils.StringUtilsWrapper;

public class NameRules {

    public static String removeEntityPostfix(String entityName){
        if(entityName != null) {
            String postfix = Configuration.has("entity_name_postfix") ? Configuration.get("entity_name_postfix").asString() : null;
            if (postfix != null && entityName.endsWith(postfix)) {
                entityName = entityName.replace(postfix, "");
            }
        }
        return entityName;
    }

    public static String removeEntityPrefix(String entityName){
        if(entityName != null) {
            String prefix = Configuration.has("entity_name_prefix") ? Configuration.get("entity_name_prefix").asString() : null;
            if (prefix != null && entityName.startsWith(prefix)) {
                entityName = entityName.replace(prefix, "");
            }
        }
        return entityName;
    }

    public static String splitByCharacterTypeCamel(String name){
       // Boolean result = ApplyRule.setRule("split_publish_by_character_type_camel_case").addContext("name",name).apply().getResult();

        if(name != null) {
            String[] newWords = StringUtilsWrapper.splitByCharacterTypeCamelCase(name);
            return StringUtils.join(newWords, "/").toLowerCase();
        }
        return name;
    }

    public static String getFirstNoun(String name){
        return new  NameGenerator().setName(name).build().getFirstNoun();
    }

    public static String getLastNoun(String name){
        return new  NameGenerator().setName(name).build().getLastNoun();
    }

    public static String getNoun(String name){
       return new  NameGenerator().setName(name).build().getNoun();
    }

    public static String getLookupDependencyName(Argument argument){
       return ApplyRule.setRule("lookup_dependency_name").addContext("argument",argument).apply().getResult();
    }

}
