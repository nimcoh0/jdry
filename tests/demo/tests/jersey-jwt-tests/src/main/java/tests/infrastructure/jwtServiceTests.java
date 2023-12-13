package tests.infrastructure;

import com.cassiomolin.example.security.jwt.model.AuthenticationToken;
import com.cassiomolin.example.security.jwt.model.UserCredentials;
import com.cassiomolin.example.user.api.model.QueryPersonResult;
import com.cassiomolin.example.user.domain.Person;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.avro.ipc.CallFuture;
import org.glassfish.jersey.client.ClientConfig;
import org.junit.Assert;
import org.softauto.core.GenericResult;
import org.softauto.jaxrs.filter.RequestClientFilter;
import org.softauto.jaxrs.cli.WebCallOption;
import org.softauto.service.JdryClient;
import org.softauto.tester.AbstractTesterImpl;
import org.softauto.tester.Listener;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@Listeners({org.softauto.testng.JdryTestListener.class})
public class jwtServiceTests extends AbstractTesterImpl {

    TestLib testLib = new TestLib();

    jwtListenerService listeners;

    JdryClient jdryClientCallback;

    JdryClient jdryClient;

    jwtService.Callback testsCallback;

    jwtService tests;

    private static final String JWT_TOKEN_HEADER_PARAM = "X-Authorization";

    ClientConfig clientConfig;

    @BeforeTest
    public void init(){
        listeners = JdryClient.create(jwtListenerService.class);
        testsCallback = JdryClient.create(jwtService.Callback.class);
        tests = JdryClient.create(jwtService.class);
        clientConfig = new ClientConfig();
        clientConfig.register(RequestClientFilter.class);
    }

    /**
     * need to capture the Token and pass it to the next call to impl JWT
     */
    @Test
    public void asyncLoginUsingJersey(){
        try {

            UserCredentials credentials = new UserCredentials();
            credentials.setUsername("user");
            credentials.setPassword("password");
            javax.ws.rs.client.Client client = javax.ws.rs.client.ClientBuilder.newClient();
            AuthenticationToken authenticationToken = client.target("http://localhost:8080").path("api").path("auth").request()
                    .post(Entity.entity(credentials, MediaType.APPLICATION_JSON), AuthenticationToken.class);
            String token = authenticationToken.getToken();

            Assert.assertTrue(token  != null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }





    /**
     * need to handle argumentsRequestType type ? like RequestBody....
     *
     */

    @Test
    public void asyncLoginUsingJdryJaxrsClientWithSetCallOptions(){
        try {
            UserCredentials credentials = new UserCredentials();
            credentials.setUsername("user");
            credentials.setPassword("password");

            HashMap<String,Object> callOption =  new WebCallOption().setMethod("POST").setProduce(MediaType.APPLICATION_JSON_TYPE)
                    .setTypes(new Class[]{UserCredentials.class}).setConsume(MediaType.APPLICATION_JSON_TYPE).setPath("/api/auth").setArgumentsNames(new String[]{"user","password"})
                    .setResponse("java.lang.Object").toMap();

            Object result = tests.com_cassiomolin_example_security_jwt_resource_AuthenticationResource_authenticate(credentials)
                    .setTransceiver("JAXRS")
                    .setCallOptions(callOption)
                    .invoke()
                    .get_Result();

            Assert.assertTrue(result != null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }





    @Test
    public void loginUsingJdryJaxrsClientProxyAndAsyncRestCallWithListenerBefore(){
        try {

            CallFuture<java.lang.String> future_com_cassiomolin_example_user_api_model_QueryPersonResult = new CallFuture();

            Listener listener = listeners.com_cassiomolin_example_greeting_service_GreetingService_getGreetingForUser();
            UserCredentials credentials = new UserCredentials();
            credentials.setUsername("admin");
            credentials.setPassword("password");


            tests.com_cassiomolin_example_security_jwt_resource_AuthenticationResource_authenticate(credentials).setExpected("#result != null").invoke().isSuccesses(res ->{
                testsCallback.com_cassiomolin_example_greeting_api_resource_GreetingResource_getProtectedGreeting(future_com_cassiomolin_example_user_api_model_QueryPersonResult).invoke()
                        .then(listener.waitTo(r -> {
                            return "aaa";
                         }));
            });

            java.lang.String result = future_com_cassiomolin_example_user_api_model_QueryPersonResult.get();
            Assert.assertTrue(result.equals("Hello aaa!"));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void loginUsingJdryJaxrsClientProxyAndAsyncRestCallWithListenerAfter(){
        try {

            CallFuture<java.lang.String> future_com_cassiomolin_example_user_api_resource_PersonResource_getUsers = new CallFuture();

            java.util.function.Consumer<Object> ff = new java.util.function.Consumer<Object>() {

                java.lang.String name = null;

                @Override
                public void accept(Object o) {
                    name = o.toString();
                }

            };

            Listener listener = listeners.com_cassiomolin_example_greeting_service_GreetingService_getGreetingForUser1();
            UserCredentials credentials = new UserCredentials();
            credentials.setUsername("admin");
            credentials.setPassword("password");


            tests.com_cassiomolin_example_security_jwt_resource_AuthenticationResource_authenticate(credentials).setExpected("#result != null").invoke().isSuccesses(res ->{
                testsCallback.com_cassiomolin_example_greeting_api_resource_GreetingResource_getProtectedGreeting1(future_com_cassiomolin_example_user_api_resource_PersonResource_getUsers).invoke()
                        .then(listener.waitTo(ff));
            });

            java.lang.String result = future_com_cassiomolin_example_user_api_resource_PersonResource_getUsers.get();
            Assert.assertTrue(result.equals("Hello admin!"));

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void asyncLoginUsingJdryJaxrsClientWithEmbededJeresy(){
        try {

            UserCredentials credentials = new UserCredentials();
            credentials.setUsername("user");
            credentials.setPassword("password");

            javax.ws.rs.client.Client client = org.softauto.jaxrs.cli.ClientBuilder.newClient();
            Future response = client.target("http://localhost:8080").path("/api/auth").request().async()
                    .post(Entity.entity(credentials, MediaType.APPLICATION_JSON),new InvocationCallback<String>() {
                        @Override
                        public void completed(String s) {
                            System.out.println("Async got: " + s);
                        }

                        @Override
                        public void failed(Throwable throwable) {
                            System.out.println("Async failure...");
                        }
                    });

            Object result = response.get();
            Assert.assertTrue(result != null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void syncLoginAndGreetingUsingJdryJaxrsClientWithEmbeddedJeresy(){
        try {
            UserCredentials credentials = new UserCredentials();
            credentials.setUsername("admin");
            credentials.setPassword("password");

            javax.ws.rs.client.Client client = org.softauto.jaxrs.cli.ClientBuilder.newClient();
            Response response2 = client.target("http://localhost:8080").path("/api/auth").request().post(Entity.entity(credentials, MediaType.APPLICATION_JSON) );
            Object o = response2.readEntity(HashMap.class);
            Response response1 = client.target("http://localhost:8080").path("api").path("greetings").path("protected").request().get();
            String o1 = response1.readEntity(String.class);
            Assert.assertTrue(o1.equals("Hello admin!"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void syncGreetingUsingJdryJaxrsClientWithEmbeddedJeresy(){
        try {
            Response response = tests.com_cassiomolin_example_greeting_api_resource_GreetingResource_getPublicGreeting().setTransceiver("JAXRS")
                    .setClientBuilder(javax.ws.rs.client.ClientBuilder.newClient((Configuration) clientConfig)).
                    target("http://localhost:8080").path("/api/greetings/public").request().get();
            Assert.assertTrue(response.readEntity(String.class).equals("Hello from the other side!"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @Test
    public void syncLoginAndGreetingUsingJdryJaxrsClientWithProxy(){
        try {
            UserCredentials credentials = new UserCredentials();
            credentials.setUsername("user");
            credentials.setPassword("password");
            AtomicReference<Object> result = new AtomicReference<>();
            tests.com_cassiomolin_example_security_jwt_resource_AuthenticationResource_authenticate(credentials).setExpected("#result != null").invoke().isSuccesses(res ->{
                result.set(tests.com_cassiomolin_example_greeting_api_resource_GreetingResource_getPublicGreeting().invoke().get_Result());
            });

            Assert.assertTrue(result.get() != null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void findAllUsersUsingJdryJaxrs(){
        try {
            UserCredentials credentials = new UserCredentials();
            credentials.setUsername("admin");
            credentials.setPassword("password");
            AtomicReference<List<Person>> people = new AtomicReference<>();
            tests.com_cassiomolin_example_security_jwt_resource_AuthenticationResource_authenticate(credentials).setExpected("#result != null").invoke().isSuccesses(res -> {
                        people.set(tests.com_cassiomolin_example_user_api_resource_PersonResource_getUsers().invoke().get_Result());
            });
            Assert.assertTrue(people.get().size()>0 );
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void publicGreetingUsingJdryRpc(){
        try {
            String  greeting =  tests.com_cassiomolin_example_greeting_api_resource_GreetingResource_getPublicGreeting().invoke().get_Result();
            Assert.assertTrue(greeting.equals("Hello from the other side!") );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void publicRpcCall(){
        try{
            String name = tests.com_cassiomolin_example_user_service_PersonService_publicRpcCall("aaaa").invoke().get_Result();
            Assert.assertTrue(name.equals("aaaa"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void privateRpcCall(){
        try{
            String name = tests.com_cassiomolin_example_user_service_PersonService_privateRpcCall("aaaa").invoke().get_Result();
            Assert.assertTrue(name.equals("aaaa"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void publicStaticRpcCall(){
        try{
            String name = tests.com_cassiomolin_example_user_service_PersonService_publicStaticRpcCall("aaaa").invoke().get_Result();
            Assert.assertTrue(name.equals("aaaa"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
