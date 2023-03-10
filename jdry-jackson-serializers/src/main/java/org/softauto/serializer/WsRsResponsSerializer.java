package org.softauto.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;

public class WsRsResponsSerializer extends StdSerializer<Response> {

    public WsRsResponsSerializer() {
        this(null);
    }

    public WsRsResponsSerializer(Class<Response> t) {
        super(t);
    }

    @Override
    public void serialize(Response response, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        HashMap<String, Object> hm = new HashMap<>();
        if(response.hasEntity()) {
            Object entity = response.getEntity();
            hm.put("realType",response.getEntity().getClass());
            hm.put("entity",entity);

            //jsonGenerator.writeStartObject();
            jsonGenerator.writeObject(hm);
        }else {
            //hm.put("status",response.getStatus());
            //hm.put("hasEntity",false);
            //hm.put("buffered",false);
            //hm.put("type",Integer.class);
            jsonGenerator.writeObject(response.getStatus());
        }
        //jsonGenerator.writeEndObject();
    }

}
