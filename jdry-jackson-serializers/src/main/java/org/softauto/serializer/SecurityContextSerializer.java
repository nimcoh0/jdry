package org.softauto.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import jakarta.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.util.HashMap;

public class SecurityContextSerializer extends StdSerializer<SecurityContext> {

    public SecurityContextSerializer() {
        this(null);
    }

    public SecurityContextSerializer(Class<SecurityContext> t) {
        super(t);
    }

    @Override
    public void serialize(SecurityContext securityContext, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        HashMap<String, String> hm = new HashMap<>();

        hm.put("Schema",securityContext.getAuthenticationScheme());
        hm.put("UserPrincipalName",securityContext.getUserPrincipal().getName());

        jsonGenerator.writeStartObject();
        if(hm.size() > 0)
            jsonGenerator.writeObjectField("SecurityContext",hm);
        jsonGenerator.writeEndObject();
    }
}
