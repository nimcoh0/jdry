package tests.infrastructure;

import com.cassiomolin.example.security.api.model.UserCredentials;
import com.cassiomolin.example.user.domain.Person;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.avro.ipc.CallFuture;
import org.junit.Assert;
import org.softauto.jaxrs.cli.WebCallOption;
import org.softauto.service.Client;
import org.softauto.tester.AbstractTesterImpl;
import org.softauto.tester.Listener;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

@Listeners(org.softauto.testng.JdryTestListener.class)
public class jwtServiceTests extends AbstractTesterImpl {

    TestLib testLib = new TestLib();

    jwtListenerService listeners;

    Client clientCallback;

    Client client;

    jwtService.Callback testsCallback;

    jwtService tests;

    @BeforeTest
    public void init(){
        listeners = (jwtListenerService) org.softauto.service.ListenerService.create(jwtListenerService.class).getListeners();
        clientCallback = Client.create(jwtService.Callback.class).build();
        client = Client.create(jwtService.class).build();
        testsCallback = (jwtService.Callback) clientCallback.getTests();
        tests = (jwtService) client.getTests();
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
            javax.ws.rs.client.Client client = javax.ws.rs.client.ClientBuilder.newBuilder().build();
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
     * need to capture the Token and pass it to the next call to impl JWT
     */
    @Test
    public void asyncLoginUsingJerseyWithListenerBefore(){
        try {
            java.util.function.Function<Object,Object> ff = new java.util.function.Function<Object,Object>() {

                @Override
                public Object apply(Object  f) {
                    try{
                        Object[] r = (Object[]) f;
                        r[1] = "ADSf";
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return f;
                }
            };


            Listener listener = listeners.com_cassiomolin_example_security_api_model_AuthenticationToken_setToken();
            UserCredentials credentials = new UserCredentials();
            credentials.setUsername("user");
            credentials.setPassword("password");


            javax.ws.rs.client.Client client = javax.ws.rs.client.ClientBuilder.newClient(); //jakarta.ws.rs.client.ClientBuilder.newBuilder().build();
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
            listener.waitTo(ff);
            Object result = response.get();
            Assert.assertTrue(result != null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    /**
     * need to handle argumentsRequestType type ? like RequestBody....
     *
     */
    @Test
    public void asyncLoginUsingJdryJaxrsClientWithListenerBefore(){
        try {
            CallFuture<javax.ws.rs.core.Response> future_com_cassiomolin_example_security_api_resource_AuthenticationResource_authenticate = new CallFuture();

            UserCredentials credentials = new UserCredentials();
            credentials.setUsername("user");
            credentials.setPassword("password");

            Listener listener = listeners.com_cassiomolin_example_security_api_model_AuthenticationToken_setToken();

            HashMap<String,Object> callOption =  new WebCallOption().setHttpMethod("POST").setProduce(MediaType.APPLICATION_JSON_TYPE)
                    .setTypes(new Class[]{UserCredentials.class}).setConsume(MediaType.APPLICATION_JSON_TYPE).setPath("/api/auth").setArgumentsNames(new String[]{"user","password"})
                    .setResponseType(javax.ws.rs.core.Response.class.getTypeName()).toMap();

            testsCallback.com_cassiomolin_example_security_api_resource_AuthenticationResource_authenticate(credentials,future_com_cassiomolin_example_security_api_resource_AuthenticationResource_authenticate)
                    .setTransceiver("JAXRS")
                    .setCallOptions(callOption)
                    .invoke()
                    .then(listener.waitTo(res ->{
                        Object[] r = (Object[]) res.result();
                        r[0] = "sdf";
                    }));
            Object result = future_com_cassiomolin_example_security_api_resource_AuthenticationResource_authenticate.get();
            Assert.assertTrue(result != null);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void asyncLoginUsingJdryJaxrsClientWithEmbededJeresyAndListenerBefore(){
        try {
            java.util.function.Function<Object,Object> ff = new java.util.function.Function<Object,Object>() {

                @Override
                public Object apply(Object  f) {
                    try{
                        Object[] r = (Object[]) f;
                        r[1] = "ADSf";
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return f;
                }
            };

                Listener listener = listeners.com_cassiomolin_example_security_api_model_AuthenticationToken_setToken();
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
                listener.waitTo(ff);
                Object result = response.get();
                Assert.assertTrue(result != null);
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
    public void syncLoginUsingJdryJaxrsClientWithEmbededJeresy(){
        try {

            UserCredentials credentials = new UserCredentials();
            credentials.setUsername("user");
            credentials.setPassword("password");

            javax.ws.rs.client.Client client = org.softauto.jaxrs.cli.ClientBuilder.newClient();
            Response response = client.target("http://localhost:8080").path("/api/auth").request()
                    .post(Entity.entity(credentials, MediaType.APPLICATION_JSON));

            Object result = response.getEntity();
            Assert.assertTrue(result != null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void syncLoginAndGreetingUsingJdryJaxrsClientWithEmbeddedJeresy(){
        try {
            UserCredentials credentials = new UserCredentials();
            credentials.setUsername("user");
            credentials.setPassword("password");

            javax.ws.rs.client.Client client = org.softauto.jaxrs.cli.ClientBuilder.newClient();
            Response response = client.target("http://localhost:8080").path("/api/auth").request().post(Entity.entity(credentials, MediaType.APPLICATION_JSON) );
            Object o = response.readEntity(HashMap.class);
            Response response1 = client.target("http://localhost:8080").path("api").path("greetings").path("public").request().get();
            String o1 = response1.readEntity(String.class);
            Assert.assertTrue(o1 != null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void findAllUsersUsingJdryRpc(){
        try {
            HashMap<String,Object> callOption = mapper.readValue("{\"constructor\":[]}",HashMap.class);
            List<Person> people =  tests.com_cassiomolin_example_user_service_UserService_findAll().setTransceiver("RPC").setCallOptions(callOption).invoke().get_Result();
            Assert.assertTrue(people.size()>0 );
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
