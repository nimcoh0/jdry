package tests.infrastructure;

import com.cassiomolin.example.security.jwt.model.AuthenticationToken;
import com.cassiomolin.example.security.jwt.model.UserCredentials;
import com.cassiomolin.example.user.domain.Person;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.softauto.core.CallFuture;
import org.glassfish.jersey.client.ClientConfig;
import org.junit.Assert;
import org.softauto.jaxrs.filter.RequestClientFilter;
import org.softauto.jaxrs.cli.WebCallOption;
import org.softauto.service.JdryClient;
import org.softauto.tester.AbstractTesterImpl;
import org.softauto.tester.Listener;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;


@Listeners({org.softauto.testng.JdryTestListener.class})
public class jwtServiceTests extends AbstractTesterImpl {

    /**
     * help class optional
     */
    TestLib testLib = new TestLib();

    /**
     * proxy listeners list
     */
    jwtListenerService listeners;

    /**
     * proxy steps with call back list
     */
    jwtService.Callback testsCallback;

    /**
     * proxy steps list
     */
    jwtService tests;

    /**
     * jersey configuration
     */
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
     * this tests run jersey in native way impl JWT authentication
     * it is given for compare and stest without the Jdry framework
     * need to capture the Token and pass it to the next step
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
     * this test show how to use proxy login step  with manual data (callOption)
     */
    @Test
    public void loginUsingJdryJaxrsClientWithSetCallOptions(){
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


    /**
     * this test show the use of two steps with Listener before (replace the listener method argument value).
     * the test will first invoke the Authentication in sync call and if success it will
     * call getProtectedGreeting in async call waiting for getGreetingForUser to be call and then replace the user name with "aaa"
     */
    @Test
    public void loginUsingJdryJaxrsClientProxyAndAsyncRestCallWithListenerBefore(){
        try {
            CallFuture<java.lang.String> future_com_cassiomolin_example_greeting_api_resource_GreetingResource_getProtectedGreeting = new CallFuture();
            Listener listener = listeners.com_cassiomolin_example_greeting_service_GreetingService_getGreetingForUser();
            UserCredentials credentials = new UserCredentials();
            credentials.setUsername("admin");
            credentials.setPassword("password");
            tests.com_cassiomolin_example_security_jwt_resource_AuthenticationResource_authenticate(credentials).setExpected("#result != null").invoke().isSuccesses(res ->{
                testsCallback.com_cassiomolin_example_greeting_api_resource_GreetingResource_getProtectedGreeting(future_com_cassiomolin_example_greeting_api_resource_GreetingResource_getProtectedGreeting).invoke()
                        .then(listener.waitTo(r -> {
                            return "aaa";
                         }));
            });
            java.lang.String result = future_com_cassiomolin_example_greeting_api_resource_GreetingResource_getProtectedGreeting.get();
            Assert.assertTrue(result.equals("Hello aaa!"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * this test show the use of two steps with Listener after (get the step result ).
     * the test will first invoke the Authentication in sync call and if success it will
     * call getProtectedGreeting1 in async call waiting for getGreetingForUser1 to be call
     */
    @Test
    public void loginUsingJdryJaxrsClientProxyAndAsyncRestCallWithListenerAfter(){
        try {

            org.softauto.core.CallFuture<java.lang.String> future_com_cassiomolin_example_greeting_service_GreetingService_getGreetingForUser1 = new org.softauto.core.CallFuture();

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
                testsCallback.com_cassiomolin_example_greeting_api_resource_GreetingResource_getProtectedGreeting1(future_com_cassiomolin_example_greeting_service_GreetingService_getGreetingForUser1).invoke()
                        .then(listener.waitTo(ff));
            });

            java.lang.String result = future_com_cassiomolin_example_greeting_service_GreetingService_getGreetingForUser1.get();
            Assert.assertTrue(result.equals("Hello admin!"));

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * the test show how to use Embedded Jersey with jdry framework
     * the configuration is done in the native jersey style but it will be exec with Jdry framework
     */
    @Test
    public void asyncLoginUsingJdryJaxrsClientWithEmbeddedJersey(){
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


    /**
     * the tests show the use of two rest call using jwt
     */
    @Test
    public void syncLoginAndGreetingUsingJdryJaxrsClientWithEmbeddedJersey(){
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


    /**
     * the test show the use of rest call using proxy step with jdry ClineBuilder
     */
    @Test
    public void syncGreetingUsingJdryJaxrsClientWithEmbeddedJersey(){
        try {
            Response response = tests.com_cassiomolin_example_greeting_api_resource_GreetingResource_getPublicGreeting().setTransceiver("JAXRS")
                    .setClientBuilder(javax.ws.rs.client.ClientBuilder.newClient((Configuration) clientConfig)).
                    target("http://localhost:8080").path("/api/greetings/public").request().get();
            Assert.assertTrue(response.readEntity(String.class).equals("Hello from the other side!"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * the test show how to use two proxy steps
     * using Jwt
     */
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

    /**
     * the test show how to use two proxy steps
     * using JWT
     */
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

    /**
     * the test show the use of rpc call
     */
    @Test
    public void publicGreetingUsingJdryJaxrs(){
        try {
            String  greeting =  tests.com_cassiomolin_example_greeting_api_resource_GreetingResource_getPublicGreeting().invoke().get_Result();
            Assert.assertTrue(greeting.equals("Hello from the other side!") );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void publicGreetingUsingJdryRpc(){
        try {
            org.softauto.core.CallFuture<java.lang.String> future_com_cassiomolin_example_user_service_PersonService_publicRpcCall = new org.softauto.core.CallFuture();
            testsCallback.com_cassiomolin_example_user_service_PersonService_publicRpcCall("aaaa",future_com_cassiomolin_example_user_service_PersonService_publicRpcCall).invoke();
            String result1 = future_com_cassiomolin_example_user_service_PersonService_publicRpcCall.get();
            Assert.assertTrue(result1.equals("aaaa") );
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * the test show the use of rpc call
     */
    @Test
    public void publicRpcCall(){
        try{
            String name = tests.com_cassiomolin_example_user_service_PersonService_publicRpcCall("aaaa").invoke().get_Result();
            Assert.assertTrue(name.equals("aaaa"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * the test show the use of rpc call to private method
     */
    @Test
    public void privateRpcCall(){
        try{
            String name = tests.com_cassiomolin_example_user_service_PersonService_privateRpcCall("aaaa").invoke().get_Result();
            Assert.assertTrue(name.equals("aaaa"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * the test show the use of rpc call to static  method
     */
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
