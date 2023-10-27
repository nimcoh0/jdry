package org.softauto.analyzer.core.utils;

import org.apache.commons.lang3.StringUtils;

public class StringUtilsWrapper extends StringUtils {



    public static String[] splitByCharacterTypeCamelCase( String str){
        String[] words = StringUtils.splitByCharacterTypeCamelCase(str);
        String[] newWords = Utils.fixsplitByCharacterTypeCamelCase(words);
      return newWords;
    }


}
