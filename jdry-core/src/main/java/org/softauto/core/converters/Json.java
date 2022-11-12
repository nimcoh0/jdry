package org.softauto.core.converters;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * convert value to type
 */
public class Json extends AbstractConverter{

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Json.class);

    @Override
    public Object build(){
        try {
            return new ObjectMapper().readValue(value.toString(), type);
        }catch (Exception e){
            logger.error("fail convert " + value.toString()+ " to "+type, e);
        }
        return null;
    }
}
