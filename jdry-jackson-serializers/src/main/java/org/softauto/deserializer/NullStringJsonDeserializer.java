package org.softauto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;

import java.io.IOException;

public class NullStringJsonDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String result = StringDeserializer.instance.deserialize(p, ctxt);
        return result!=null && result.toLowerCase().equals(null+"") ? null : result;
    }
}
