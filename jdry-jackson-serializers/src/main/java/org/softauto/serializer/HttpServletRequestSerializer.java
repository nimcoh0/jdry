package org.softauto.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;
import org.apache.commons.io.IOUtils;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        try {
            Enumeration enumeration = httpServletRequest.getAttributeNames();
            for (Iterator it = enumeration.asIterator(); it.hasNext(); ) {
                String name = it.next().toString();
                if(httpServletRequest.getAttribute(name) != null) {
                    String value = httpServletRequest.getAttribute(name).toString();
                    headers.put(name, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return headers;
    }

    @Override
    public void serialize(HttpServletRequest httpServletRequest, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        HashMap<String, Object> hm = new HashMap<>();
       // byte[] cachedBody;
        //String requestStr = IOUtils.toString(httpServletRequest.getInputStream());
        //InputStream inputStream = httpServletRequest.getInputStream();
        //cachedBody = StreamUtils.copyToByteArray(inputStream);
        //String requestStr = IOUtils.toString(httpServletRequest.getReader()); //IOUtils.toString(inputStream);
        //String requestStr = IOUtils.toString(cachedBody);
        //hm.put("requestStr",requestStr);

        //String s = ByteSource.wrap(ByteStreams.toByteArray(httpServletRequest.getInputStream())).asCharSource(Charsets.UTF_8).read();
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
        //hm.put("type",HttpServletRequest.class);
        hm.put("Attributes",getAttributes(httpServletRequest));
        hm.put("Method",httpServletRequest.getMethod());
        //BufferedReader bufferedReader = httpServletRequest.getReader();
        //ServletRequest servletRequest = ((HttpServletRequestWrapper)httpServletRequest).getRequest();
        //ServletRequest servletRequest1 = new ServletRequest() new HttpServletRequestWrapper((HttpServletRequest) servletRequest)
        //hm.put("ParameterNames",new ObjectMapper().writeValueAsString(httpServletRequest.getParameterNames()));
        //InputStream inputStream = httpServletRequest.getInputStream();
        //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        //ServletInputStream servletInputStream = httpServletRequest.getInputStream();

        //jsonGenerator.writeStartObject();
        if(hm.size() > 0) {
            HashMap<String, Object> hm1 = new HashMap<>();
            hm1.put("javax.servlet.http.HttpServletRequest", hm);
            hm1.put("realType",HttpServletRequest.class);
            jsonGenerator.writeObject(hm1);
            //jsonGenerator.writeObjectField("javax.servlet.http.HttpServletRequest", hm);
        }
        //jsonGenerator.writeEndObject();
    }
}
