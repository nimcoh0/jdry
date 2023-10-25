package org.softauto.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DuplicateObjectSerializer extends StdSerializer<Object> {

    private JsonSerializer<Object> defaultSerializer;



    private List<String> hashcodes = new ArrayList<>();


    public DuplicateObjectSerializer() {
        super(Object.class);
    }


    public DuplicateObjectSerializer(JsonSerializer<Object> defaultSerializer) {
        super(Object.class);
        this.defaultSerializer = defaultSerializer;
    }



    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try {
            String hashcode = Integer.toHexString(value.hashCode());
            if (defaultSerializer instanceof BeanSerializer) {
                if(!hashcodes.contains(hashcode)){
                    hashcodes.add(hashcode);
                    defaultSerializer.serialize(value, gen, provider);
                }else {
                    System.out.println("droping " + hashcode+ " "+ value.toString());
                    new StringSerializer().serialize("value", gen, provider);
                }
            } else {
                defaultSerializer.serialize(value, gen, provider);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
