package org.softauto.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

public class HttpServletResponseSerializer extends StdSerializer<HttpServletResponse> {

    public HttpServletResponseSerializer() {
        this(null);
    }

    public HttpServletResponseSerializer(Class<HttpServletResponse> t) {
        super(t);
    }



    @Override
    public void serialize(HttpServletResponse httpServletResponse, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        HashMap<String, Object> hm = new HashMap<>();
        HttpServletResponseWrapper httpServletResponseWrapper =(HttpServletResponseWrapper)httpServletResponse;
        httpServletResponseWrapper.setHeader("Cache-Control", "public, max-age=86400");
        ServletResponse response = httpServletResponseWrapper.getResponse();
        //StringWriter stringWriter = new StringWriter();
        //PrintWriter printWriter = new PrintWriter(stringWriter);

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        //response.getWriter().write("<html><body>Hello World!</body></html>");
        response.flushBuffer();
        ServletOutputStream servletOutputStream = response.getOutputStream();
        String responseData = servletOutputStream.toString();
        //ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //PrintStream errorOut = new PrintStream(byteArrayOutputStream);
        //byteArrayOutputStream.
       // String responseData = stringWriter.toString();

        //if(hm.size() > 0) {
            HashMap<String, Object> hm1 = new HashMap<>();
            hm1.put("javax.servlet.http.HttpServletResponse", hm);
            hm1.put("type",HttpServletResponse.class);
            jsonGenerator.writeObject(hm1);
            //jsonGenerator.writeObjectField("javax.servlet.http.HttpServletRequest", hm);
        //}
        //jsonGenerator.writeEndObject();
        response.getWriter().write(responseData);
    }
}
