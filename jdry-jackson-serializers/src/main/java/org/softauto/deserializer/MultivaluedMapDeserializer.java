package org.softauto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import jakarta.ws.rs.core.MultivaluedMap;
import java.io.IOException;

public class MultivaluedMapDeserializer extends JsonDeserializer<jakarta.ws.rs.core.MultivaluedMap> {


    @Override
    public MultivaluedMap deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return null;
    }
}
