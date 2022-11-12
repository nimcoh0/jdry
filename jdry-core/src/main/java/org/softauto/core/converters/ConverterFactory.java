package org.softauto.core.converters;

public class ConverterFactory {

    public static AbstractConverter getConverter(String type)throws Exception{
        if(type.equals("json")){
            return new Json();
        }
        throw new Exception("not supported converter type "+ type);
    }

}
