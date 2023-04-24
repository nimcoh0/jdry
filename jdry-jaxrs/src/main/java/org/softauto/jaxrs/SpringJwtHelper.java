package org.softauto.jaxrs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.message.internal.OutboundJaxrsResponse;
import org.softauto.core.TestContext;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class SpringJwtHelper implements ClientHttpRequestInterceptor, Closeable {

    public static RestTemplate restTemplate =  new RestTemplate();
    private static String token;
    private static String refreshToken;
    private static final String JWT_TOKEN_HEADER_PARAM = "X-Authorization";
    protected  String baseURL;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public SpringJwtHelper() {

    }

    public SpringJwtHelper(RestTemplate restTemplate, String baseURL) {
        this.restTemplate = restTemplate;
        this.baseURL = baseURL;
    }

    public void refreshToken() {
        Map<String, String> refreshTokenRequest = new HashMap<>();
        refreshTokenRequest.put("refreshToken", refreshToken);
        ResponseEntity<JsonNode> tokenInfo = restTemplate.postForEntity(baseURL + "/api/auth/token", refreshTokenRequest, JsonNode.class);
        setTokenInfo(tokenInfo.getBody());
    }

    private void setTokenInfo(JsonNode tokenInfo) {
        this.token = tokenInfo.get("token").asText();
        this.refreshToken = tokenInfo.get("refreshToken").asText();
        restTemplate.getInterceptors().add(this);
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] bytes, ClientHttpRequestExecution execution) throws IOException {
        HttpRequest wrapper = new HttpRequestWrapper(request);
        wrapper.getHeaders().set(JWT_TOKEN_HEADER_PARAM, "Bearer " + token);
        ClientHttpResponse response = execution.execute(wrapper, bytes);
        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            synchronized (this) {
                restTemplate.getInterceptors().remove(this);
                refreshToken();
                wrapper.getHeaders().set(JWT_TOKEN_HEADER_PARAM, "Bearer " + token);
                return execution.execute(wrapper, bytes);
            }
        }
        return response;
    }

    public Object delete(String url, Object[] args)throws Exception{
        try {
            restTemplate.delete(url, args[0]);
            return Response.ok();
        } catch (RestClientException e) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(),e.getMessage());
        }
    }


    public Object post(String url,  Object entity, Class returnType,Object[] args)throws Exception{
        Object response = null;
        ResponseEntity<JsonNode> tokenInfo = null;
        if(token != null) {
            response = restTemplate.postForEntity(url, args[0], returnType).getBody();
        }else {
            Map<String, String> loginRequest = new HashMap<>();
            loginRequest.put("username", args[0].toString());
            loginRequest.put("password", args[1].toString());
            tokenInfo = restTemplate.postForEntity(url, loginRequest, JsonNode.class);
            setTokenInfo(tokenInfo.getBody());
            if(Response.Status.fromStatusCode(tokenInfo.getStatusCodeValue()).getFamily() == Response.Status.Family.SUCCESSFUL) {
                response = tokenInfo.getBody();
            }else {
                response = tokenInfo.getStatusCodeValue();
            }
        }

        return response;
    }


    @Override
    public void close() throws IOException {

    }
}
