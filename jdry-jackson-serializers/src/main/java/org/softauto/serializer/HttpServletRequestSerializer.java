package org.softauto.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

public class HttpServletRequestSerializer extends StdSerializer<HttpServletRequest> {

    public HttpServletRequestSerializer() {
        this(null);
    }

    public HttpServletRequestSerializer(Class<HttpServletRequest> t) {
        super(t);
    }

    private HashMap<String,Object> getHeaders(HttpServletRequest httpServletRequest){
        HashMap<String,Object> headers = new HashMap<>();
        Enumeration enumeration = httpServletRequest.getHeaderNames();
        for (Iterator it = enumeration.asIterator(); it.hasNext(); ) {
            String name = it.next().toString();
            String value = httpServletRequest.getHeader(name);
            headers.put(name,value);
        }
        return headers;
    }

    private HashMap<String,Object> getAttributes(HttpServletRequest httpServletRequest){
        HashMap<String,Object> headers = new HashMap<>();
        Enumeration enumeration = httpServletRequest.getAttributeNames();
        for (Iterator it = enumeration.asIterator(); it.hasNext(); ) {
            String name = it.next().toString();
            String value = httpServletRequest.getHeader(name);
            headers.put(name,value);
        }
        return headers;
    }

    @Override
    public void serialize(HttpServletRequest httpServletRequest, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        HashMap<String, Object> hm = new HashMap<>();

        hm.put("Schema",httpServletRequest.getScheme());
        hm.put("ParameterMap",new ObjectMapper().writeValueAsString(httpServletRequest.getParameterMap()));
        hm.put("Content-Type",httpServletRequest.getContentType());
        hm.put("PathInfo",httpServletRequest.getPathInfo());
        hm.put("RequestURI",httpServletRequest.getRequestURI());
        hm.put("RequestURL",httpServletRequest.getRequestURL().toString());
        hm.put("ContextPath",httpServletRequest.getContextPath());
        hm.put("ServletPath",httpServletRequest.getServletPath());
        hm.put("SessionId",httpServletRequest.getSession().getId());
        hm.put("Protocol",httpServletRequest.getProtocol());
        hm.put("headers",getHeaders(httpServletRequest));
        //hm.put("Attributes",getAttributes(httpServletRequest));
        hm.put("Method",httpServletRequest.getMethod());
        //hm.put("ParameterNames",new ObjectMapper().writeValueAsString(httpServletRequest.getParameterNames()));
        //InputStream inputStream = httpServletRequest.getInputStream();
        //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        //ServletInputStream servletInputStream = httpServletRequest.getInputStream();
        //String requestStr = IOUtils.toString(httpServletRequest.getInputStream());
        //String s =ByteSource.wrap(ByteStreams.toByteArray(httpServletRequest.getInputStream())).asCharSource(Charsets.UTF_8).read();
        jsonGenerator.writeStartObject();
        if(hm.size() > 0)
            jsonGenerator.writeObjectField("javax.servlet.http.HttpServletRequest",hm);
        jsonGenerator.writeEndObject();
    }
}
