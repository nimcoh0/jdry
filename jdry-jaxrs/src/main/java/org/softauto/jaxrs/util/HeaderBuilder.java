package org.softauto.jaxrs.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.HashMap;
import java.util.Map;

public class HeaderBuilder {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(HeaderBuilder.class);

    MultivaluedMap<String, Object> headers; ;

    public HeaderBuilder(MultivaluedMap<String, Object> headers){
        this.headers = headers;
    }

    public MultivaluedMap<String, Object> getHeaders() {
        return headers;
    }

    public static Builder newBuilder (){
        return new Builder();
    }

    public static class Builder{

        JsonNode headers;


        public Builder setHeader(JsonNode headers) {
            this.headers = headers;
            return this;
        }



        public HeaderBuilder build(){
            MultivaluedMap<String, Object> _headers = null;
            try {
                if (headers != null && headers.isContainerNode() && !headers.isEmpty()) {
                    String str = new ObjectMapper().writeValueAsString(headers);
                    HashMap<String, String> hm = new ObjectMapper().readValue(str, HashMap.class);
                    _headers =buildHeaders(hm);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return new HeaderBuilder(_headers);
        }

        protected MultivaluedMap<String, Object> buildHeaders(Map<String, String> header) {
            MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
            header.forEach((k, v) -> {
                headers.add(k, v);
            });
            return headers;
        }
    }
}
