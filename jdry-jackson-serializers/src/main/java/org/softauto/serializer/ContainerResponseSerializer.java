package org.softauto.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.glassfish.jersey.server.ContainerResponse;

import java.io.IOException;
import java.util.HashMap;

public class ContainerResponseSerializer extends StdSerializer<ContainerResponse> {

    public ContainerResponseSerializer() {
        this(null);
    }

    public ContainerResponseSerializer(Class<ContainerResponse> t) {
        super(t);
    }


    @Override
    public void serialize(ContainerResponse containerResponse, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        HashMap<String, Object> hm = new HashMap<>();

        hm.put("Headers", containerResponse.getHeaders());
        hm.put("Cookies", containerResponse.getCookies());
        hm.put("MediaType", containerResponse.getMediaType());
        hm.put("isChunked", containerResponse.isChunked());
        hm.put("statue", containerResponse.getStatus());

        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("response", hm);
        jsonGenerator.writeEndObject();
    }
}
