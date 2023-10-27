package org.softauto.analyzer.core.rules;

import org.apache.commons.lang3.math.NumberUtils;
import org.softauto.analyzer.model.test.Test;

public class AssertRules {

    public static String buildExpression(Test test){
       //return getDefaultAsertByType(test.getResultType(),test.getResult());
       return getDefaultAsertByType(test.getExpected().getType(),test.getExpected().getName());
    }

    public static String getDefaultAsertByType( String type, String value ) {
        try {
            if(type != null&& value != null) {
                String str = type.toLowerCase();
                if (type.contains(".")) {
                    str = type.substring(type.lastIndexOf(".") + 1).toLowerCase();
                }
                if (value.endsWith(";")) {
                    value = value.substring(0, value.length() - 1);
                }
                if (NumberUtils.isCreatable(value)) {
                    return value;
                }
                if (value.toLowerCase().equals("false") || value.toLowerCase().equals("true")) {
                    return value;
                }


                if (str.equals("boolean"))
                    return "org.junit.Assert.assertTrue("+value+")";
                if (str.equals("byte"))
                    return "org.junit.Assert.assertTrue("+value+" != null)";
                if (str.equals("short"))
                    return "org.junit.Assert.assertTrue("+value+" != -1)";
                if (str.equals("integer"))
                    return "org.junit.Assert.assertTrue("+value+" != -1)";
                if (str.equals("int"))
                    return "org.junit.Assert.assertTrue("+value +" != -1)";
                if (str.equals("long"))
                    return "org.junit.Assert.assertTrue("+value +" != -1L)";
                if (str.equals("float"))
                    return "org.junit.Assert.assertTrue("+value+"  > -1.0)";
                if (str.equals("double"))
                    return "org.junit.Assert.assertTrue("+value +" > -1.0)";
                if (str.equals("string")) return "org.junit.Assert.assertTrue("+value+" != null)";
                return "org.junit.Assert.assertTrue("+value+" != null)";
            }
        } catch (Exception e) {
            e.printStackTrace();
           // logger.error("fail get Default Assert By Type ",e);
        }
        return null;
    }
}
