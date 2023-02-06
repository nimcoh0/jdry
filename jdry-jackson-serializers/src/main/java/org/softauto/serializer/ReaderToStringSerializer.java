package org.softauto.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.io.Reader;

public class ReaderToStringSerializer extends JsonSerializer<Reader> {


    @Override
    public void serialize(Reader reader, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        StringBuilder buffer = new StringBuilder();
        char[] array = new char[8096];
        int len = 0;
        while (-1 != (len = reader.read(array))) {
            buffer.append(array,0,len);
        }
        reader.close();
        jsonGenerator.writeString(buffer.toString());
    }
}
