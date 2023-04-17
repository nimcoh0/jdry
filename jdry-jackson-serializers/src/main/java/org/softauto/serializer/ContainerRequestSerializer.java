package org.softauto.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.glassfish.jersey.message.internal.ReaderWriter;
import org.glassfish.jersey.server.ContainerRequest;
import org.apache.commons.codec.binary.Base64;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ContainerRequestSerializer extends StdSerializer<ContainerRequest> {

    public ContainerRequestSerializer() {
        this(null);
    }

    public ContainerRequestSerializer(Class<ContainerRequest> t) {
        super(t);
    }

    private String getEntityBody(ContainerRequestContext requestContext) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //InputStream in = requestContext.getEntityStream();
        ContainerRequest containerRequest = (ContainerRequest)requestContext;
        InputStream in = containerRequest.readEntity(InputStream.class);
        String result = null;
        try {
            ReaderWriter.writeTo(in, out);

            byte[] requestEntity = out.toByteArray();
            if (requestEntity.length == 0) {
                result = "";
            } else {
                result = new String(requestEntity, "UTF-8");
            }
            requestContext.setEntityStream(new ByteArrayInputStream(requestEntity));
            //System.out.println("recording entity ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private HashMap<String,Object> getProperties(ContainerRequest requestContext){
        HashMap<String,Object> properties = new HashMap<>();
        Collection<String> propertyNames = requestContext.getPropertyNames();
        for(String name : propertyNames){
            properties.put(name,requestContext.getProperty(name));
        }

        return properties;
    }



    @Override
    public void serialize(ContainerRequest requestContext, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
         try {
            HashMap<String, Object> hm = new HashMap<>();
            //Object body = null;

            try {
                if (requestContext.getRequestHeader("Content-Length") != null) {
                    String s = getEntityBody(requestContext);
                    if(requestContext.getMediaType().getSubtype().equals("json")){
                        hm.put("entity", new ObjectMapper().readTree(s));
                    }else {
                        hm.put("entity", s);
                    }
                }
            }catch (Throwable e){
                System.out.println("no entity or stream is close ");
            }
            // hm.put("entity", body);
             hm.put("RequestUri", requestContext.getRequestUri().toString());
             hm.put("AbsolutePath", requestContext.getAbsolutePath().toString());
             hm.put("BaseUri", requestContext.getBaseUri().toString());
             //hm.put("Properties", requestContext.getBaseUri().toString());
             hm.put("MediaType", requestContext.getMediaType());
             hm.put("RequestHeaders", requestContext.getRequestHeaders());
             hm.put("Headers", requestContext.getHeaders());
             //hm.put("ResponseCookies", requestContext.getResponseCookies());
             //hm.put("properties",getProperties(requestContext));
             //hm.put("AuthenticationScheme", (GrizzlyHttpContainer)requestContext.getSecurityContext();
             hm.put("Method", requestContext.getMethod());

             //hm.put("type",ContainerRequest.class);
             MultivaluedMap<String, String> headers = ((ContainerRequestContext) requestContext).getHeaders();
             List<String> authorization = headers.get("Authorization");
             if (authorization != null && !authorization.isEmpty()) {
                 String encodedUserPassword = authorization.get(0).replaceFirst("Basic" + " ", "");
                 Base64 base64 = new Base64();

                 String usernameAndPassword = new String(base64.decode(encodedUserPassword.getBytes()));

                 StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
                 hm.put("username", tokenizer.nextToken());
                 hm.put("password", tokenizer.nextToken());

             }


            /*
            String media = null;
            if(requestContext.getMediaType() != null) {
                media = requestContext.getMediaType().toString();
            }


            MultivaluedMap<String, String> headers = ((ContainerRequestContext) requestContext).getHeaders();


            if (media != null && media.equals("application/x-www-form-urlencoded;charset=UTF-8")) {
                if (requestContext.hasEntity()) {
                    requestContext.bufferEntity();
                }
                String schema = ((ContainerRequestContext) requestContext).getSecurityContext().getAuthenticationScheme();
                String connection = ((LinkedList)headers.get("Connection")).get(0).toString();
                String result = getEntityBody(requestContext);
                Properties p = new Properties();
                String[] props = result.split("&");
                for (String s : props) {
                    p.load(new StringReader(s));
                    p.forEach((k, v) -> {
                        hm.put(k.toString(), v.toString());
                    });
                }
                hm.put("media", "application/x-www-form-urlencoded;charset=UTF-8");
                hm.put("schema", "WWW-Authenticate");



            }

            List<String> authorization = headers.get("Authorization");
            if (authorization != null && !authorization.isEmpty()) {
                String encodedUserPassword = authorization.get(0).replaceFirst("Basic" + " ", "");
                Base64 base64 = new Base64();

                String usernameAndPassword = new String(base64.decode(encodedUserPassword.getBytes()));

                StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
                hm.put("username", tokenizer.nextToken());
                hm.put("password", tokenizer.nextToken());
                hm.put("schema", "basic");
                hm.put("media", "application/json");
                //hm.put("connection", connection);
            }


             */

            //jsonGenerator.writeStartObject();

            if(hm.size() > 0) {
                HashMap<String, Object> hm1 = new HashMap<>();
                hm1.put("request", hm);
                hm1.put("realType", ContainerRequest.class);
                //jsonGenerator.writeObjectField("requestContext", hm);
                jsonGenerator.writeObject(hm1);

                //jsonGenerator.writeEndObject();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
