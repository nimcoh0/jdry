package org.softauto.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

public class HttpServletRequestWrapperSerializer extends StdSerializer<HttpServletRequestWrapper> {

    public HttpServletRequestWrapperSerializer() {
        this(null);
    }

    public HttpServletRequestWrapperSerializer(Class<HttpServletRequestWrapper> t) {
        super(t);
    }


    @Override
    public void serialize(HttpServletRequestWrapper httpServletRequestWrapper, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        byte[] cachedBody;
        ServletRequest request = httpServletRequestWrapper.getRequest();
        HttpServletRequestWrapper h = new HttpServletRequestWrapper((HttpServletRequest) request);

        cachedBody = StreamUtils.copyToByteArray(h.getInputStream());

        //httpServletRequestWrapper.setRequest(request);

    }
}
