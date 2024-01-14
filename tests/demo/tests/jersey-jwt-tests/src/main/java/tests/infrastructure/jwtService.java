/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package tests.infrastructure;

public interface jwtService {
    public static final org.softauto.core.Analyze analyze = org.softauto.core.Analyze.parse("{\"name\":\"jwt\",\"namespace\":\"tests.infrastructure\",\"steps\":[{\"name\":\"getPublicGreeting\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.greeting.api.resource.GreetingResource\",\"namespaceChange\":false,\"annotations\":{\"javax.ws.rs.GET\":{},\"javax.ws.rs.Path\":{\"value\":\"public\",\"type\":\"java.lang.String\"},\"javax.ws.rs.Produces\":{\"value\":\"text/plain\",\"type\":\"java.lang.String\"},\"javax.annotation.security.PermitAll\":{},\"class\":{\"javax.enterprise.context.RequestScoped\":{},\"javax.ws.rs.Path\":{\"value\":\"greetings\",\"type\":\"java.lang.String\"}}},\"parametersTypes\":[],\"id\":29393,\"returnType\":\"javax.ws.rs.core.Response\",\"unboxReturnType\":\"java.lang.String\",\"childes\":[{\"name\":\"getPublicGreeting\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.greeting.service.GreetingService\",\"namespaceChange\":false,\"annotations\":{\"org.softauto.annotations.ApiForTesting\":{\"protocol\":\"RPC\",\"type\":\"java.lang.String\"},\"class\":{\"javax.enterprise.context.ApplicationScoped\":{}}},\"parametersTypes\":[],\"id\":29397,\"returnType\":\"java.lang.String\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[],\"modifier\":0,\"constructorParameter\":[],\"properties\":{},\"responseChain\":[],\"annotationsHelper\":[],\"crudToSubject\":[]}],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"path\":\"/api/greetings/public\",\"argumentsNames\":[],\"protocol\":\"JAXRS\",\"method\":\"GET\",\"response\":\"java.lang.String\",\"produces\":\"text/plain\",\"consumes\":\"\"},\"responseChain\":[\"javax.ws.rs.core.Response\"],\"annotationsHelper\":[],\"crudToSubject\":[],\"fullname\":\"com_cassiomolin_example_greeting_api_resource_GreetingResource_getPublicGreeting\"},{\"name\":\"getProtectedGreeting\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.greeting.api.resource.GreetingResource\",\"namespaceChange\":false,\"annotations\":{\"javax.ws.rs.GET\":{},\"javax.ws.rs.Path\":{\"value\":\"protected\",\"type\":\"java.lang.String\"},\"javax.ws.rs.Produces\":{\"value\":\"text/plain\",\"type\":\"java.lang.String\"},\"class\":{\"javax.enterprise.context.RequestScoped\":{},\"javax.ws.rs.Path\":{\"value\":\"greetings\",\"type\":\"java.lang.String\"}}},\"parametersTypes\":[],\"id\":29394,\"returnType\":\"javax.ws.rs.core.Response\",\"unboxReturnType\":\"java.lang.String\",\"childes\":[{\"name\":\"getGreetingForUser\",\"type\":\"listener\",\"namespce\":\"com.cassiomolin.example.greeting.service.GreetingService\",\"namespaceChange\":false,\"annotations\":{\"org.softauto.annotations.ListenerForTesting\":{\"type\":\"BEFORE\"},\"class\":{\"javax.enterprise.context.ApplicationScoped\":{}}},\"parametersTypes\":[\"java.lang.String\"],\"id\":29398,\"returnType\":\"java.lang.String\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[\"username\"],\"modifier\":0,\"constructorParameter\":[],\"properties\":{},\"responseChain\":[],\"annotationsHelper\":[],\"crudToSubject\":[]}],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"path\":\"/api/greetings/protected\",\"argumentsNames\":[],\"protocol\":\"JAXRS\",\"method\":\"GET\",\"response\":\"java.lang.String\",\"produces\":\"text/plain\",\"consumes\":\"\"},\"responseChain\":[\"javax.ws.rs.core.Response\"],\"annotationsHelper\":[],\"crudToSubject\":[],\"fullname\":\"com_cassiomolin_example_greeting_api_resource_GreetingResource_getProtectedGreeting\"},{\"name\":\"getProtectedGreeting1\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.greeting.api.resource.GreetingResource\",\"namespaceChange\":false,\"annotations\":{\"javax.ws.rs.GET\":{},\"javax.ws.rs.Path\":{\"value\":\"protected1\",\"type\":\"java.lang.String\"},\"javax.ws.rs.Produces\":{\"value\":\"text/plain\",\"type\":\"java.lang.String\"},\"class\":{\"javax.enterprise.context.RequestScoped\":{},\"javax.ws.rs.Path\":{\"value\":\"greetings\",\"type\":\"java.lang.String\"}}},\"parametersTypes\":[],\"id\":29395,\"returnType\":\"javax.ws.rs.core.Response\",\"unboxReturnType\":\"java.lang.String\",\"childes\":[{\"name\":\"getGreetingForUser1\",\"type\":\"listener\",\"namespce\":\"com.cassiomolin.example.greeting.service.GreetingService\",\"namespaceChange\":false,\"annotations\":{\"org.softauto.annotations.ListenerForTesting\":{\"type\":\"AFTER\"},\"class\":{\"javax.enterprise.context.ApplicationScoped\":{}}},\"parametersTypes\":[\"java.lang.String\"],\"id\":29399,\"returnType\":\"java.lang.String\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[\"username\"],\"modifier\":0,\"constructorParameter\":[],\"properties\":{},\"responseChain\":[],\"annotationsHelper\":[],\"crudToSubject\":[]}],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"path\":\"/api/greetings/protected1\",\"argumentsNames\":[],\"protocol\":\"JAXRS\",\"method\":\"GET\",\"response\":\"java.lang.String\",\"produces\":\"text/plain\",\"consumes\":\"\"},\"responseChain\":[\"javax.ws.rs.core.Response\"],\"annotationsHelper\":[],\"crudToSubject\":[],\"fullname\":\"com_cassiomolin_example_greeting_api_resource_GreetingResource_getProtectedGreeting1\"},{\"name\":\"getPublicGreeting\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.greeting.service.GreetingService\",\"namespaceChange\":false,\"annotations\":{\"org.softauto.annotations.ApiForTesting\":{\"protocol\":\"RPC\",\"type\":\"java.lang.String\"},\"class\":{\"javax.enterprise.context.ApplicationScoped\":{}}},\"parametersTypes\":[],\"id\":29397,\"returnType\":\"java.lang.String\",\"unboxReturnType\":\"java.lang.String\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"protocol\":\"RPC\"},\"responseChain\":[],\"annotationsHelper\":[],\"crudToSubject\":[],\"fullname\":\"com_cassiomolin_example_greeting_service_GreetingService_getPublicGreeting\"},{\"name\":\"authenticate\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.security.jwt.resource.AuthenticationResource\",\"namespaceChange\":false,\"annotations\":{\"javax.ws.rs.POST\":{},\"javax.ws.rs.Consumes\":{\"value\":\"application/json\",\"type\":\"java.lang.String\"},\"javax.ws.rs.Produces\":{\"value\":\"application/json\",\"type\":\"java.lang.String\"},\"javax.annotation.security.PermitAll\":{},\"class\":{\"javax.enterprise.context.RequestScoped\":{},\"javax.ws.rs.Path\":{\"value\":\"auth\",\"type\":\"java.lang.String\"}}},\"parametersTypes\":[\"com.cassiomolin.example.security.jwt.model.UserCredentials\"],\"id\":29647,\"returnType\":\"javax.ws.rs.core.Response\",\"unboxReturnType\":\"com.cassiomolin.example.security.jwt.model.AuthenticationToken\",\"returnTypeName\":\"authenticationToken\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":true,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[\"credentials\"],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"path\":\"/api/auth/\",\"argumentsRequestType\":{\"FormParam\":[{\"index\":0,\"name\":\"credentials\",\"type\":\"com.cassiomolin.example.security.jwt.model.UserCredentials\"}]},\"argumentsNames\":[\"credentials\"],\"protocol\":\"JAXRS\",\"method\":\"POST\",\"response\":\"com.cassiomolin.example.security.jwt.model.AuthenticationToken\",\"produces\":\"application/json\",\"consumes\":\"application/json\"},\"responseChain\":[\"javax.ws.rs.core.Response\"],\"annotationsHelper\":[],\"crudToSubject\":[],\"fullname\":\"com_cassiomolin_example_security_jwt_resource_AuthenticationResource_authenticate\"},{\"name\":\"refresh\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.security.jwt.resource.AuthenticationResource\",\"namespaceChange\":false,\"annotations\":{\"javax.ws.rs.POST\":{},\"javax.ws.rs.Path\":{\"value\":\"refresh\",\"type\":\"java.lang.String\"},\"javax.ws.rs.Produces\":{\"value\":\"application/json\",\"type\":\"java.lang.String\"},\"class\":{\"javax.enterprise.context.RequestScoped\":{},\"javax.ws.rs.Path\":{\"value\":\"auth\",\"type\":\"java.lang.String\"}}},\"parametersTypes\":[],\"id\":29648,\"returnType\":\"javax.ws.rs.core.Response\",\"unboxReturnType\":\"com.cassiomolin.example.security.jwt.model.AuthenticationToken\",\"returnTypeName\":\"authenticationToken\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"path\":\"/api/auth/refresh\",\"argumentsNames\":[],\"protocol\":\"JAXRS\",\"method\":\"POST\",\"response\":\"com.cassiomolin.example.security.jwt.model.AuthenticationToken\",\"produces\":\"application/json\",\"consumes\":\"\"},\"responseChain\":[\"javax.ws.rs.core.Response\"],\"annotationsHelper\":[],\"crudToSubject\":[],\"fullname\":\"com_cassiomolin_example_security_jwt_resource_AuthenticationResource_refresh\"},{\"name\":\"getUsers\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.user.api.resource.PersonResource\",\"namespaceChange\":false,\"annotations\":{\"javax.ws.rs.GET\":{},\"javax.ws.rs.Produces\":{\"value\":\"application/json\",\"type\":\"java.lang.String\"},\"javax.annotation.security.RolesAllowed\":{\"value\":\"ADMIN\",\"type\":\"java.lang.String\"},\"class\":{\"javax.enterprise.context.RequestScoped\":{},\"javax.ws.rs.Path\":{\"value\":\"users\",\"type\":\"java.lang.String\"}}},\"parametersTypes\":[],\"id\":29670,\"returnType\":\"javax.ws.rs.core.Response\",\"unboxReturnType\":\"java.util.List\",\"returnTypeName\":\"queryPersonResults\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"path\":\"/api/users/\",\"argumentsNames\":[],\"protocol\":\"JAXRS\",\"method\":\"GET\",\"response\":\"java.util.List\",\"produces\":\"application/json\",\"consumes\":\"\"},\"responseChain\":[\"javax.ws.rs.core.Response\"],\"annotationsHelper\":[],\"crudToSubject\":[],\"fullname\":\"com_cassiomolin_example_user_api_resource_PersonResource_getUsers\"},{\"name\":\"getUser\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.user.api.resource.PersonResource\",\"namespaceChange\":false,\"annotations\":{\"javax.ws.rs.GET\":{},\"javax.ws.rs.Path\":{\"value\":\"{userId}\",\"type\":\"java.lang.String\"},\"javax.ws.rs.Produces\":{\"value\":\"application/json\",\"type\":\"java.lang.String\"},\"javax.annotation.security.RolesAllowed\":{\"value\":\"ADMIN\",\"type\":\"java.lang.String\"},\"javax.ws.rs.PathParam\":{\"value\":\"userId\",\"type\":\"java.lang.String\",\"index\":0},\"class\":{\"javax.enterprise.context.RequestScoped\":{},\"javax.ws.rs.Path\":{\"value\":\"users\",\"type\":\"java.lang.String\"}}},\"parametersTypes\":[\"java.lang.Long\"],\"id\":29671,\"returnType\":\"javax.ws.rs.core.Response\",\"unboxReturnType\":\"com.cassiomolin.example.user.api.model.QueryPersonResult\",\"returnTypeName\":\"queryPersonResult\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":true,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[\"userId\"],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"path\":\"/api/users/{userId}\",\"argumentsRequestType\":{\"PathParam\":[{\"index\":0,\"name\":\"userId\",\"type\":\"java.lang.Long\"}]},\"argumentsNames\":[\"userId\"],\"protocol\":\"JAXRS\",\"method\":\"GET\",\"response\":\"com.cassiomolin.example.user.api.model.QueryPersonResult\",\"produces\":\"application/json\",\"consumes\":\"\"},\"responseChain\":[\"javax.ws.rs.core.Response\"],\"annotationsHelper\":[],\"crudToSubject\":[],\"fullname\":\"com_cassiomolin_example_user_api_resource_PersonResource_getUser\"},{\"name\":\"getAuthenticatedUser\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.user.api.resource.PersonResource\",\"namespaceChange\":false,\"annotations\":{\"javax.ws.rs.GET\":{},\"javax.ws.rs.Path\":{\"value\":\"me\",\"type\":\"java.lang.String\"},\"javax.ws.rs.Produces\":{\"value\":\"application/json\",\"type\":\"java.lang.String\"},\"javax.annotation.security.PermitAll\":{},\"class\":{\"javax.enterprise.context.RequestScoped\":{},\"javax.ws.rs.Path\":{\"value\":\"users\",\"type\":\"java.lang.String\"}}},\"parametersTypes\":[],\"id\":29672,\"returnType\":\"javax.ws.rs.core.Response\",\"unboxReturnType\":\"com.cassiomolin.example.user.api.model.QueryPersonResult\",\"returnTypeName\":\"queryPersonResult_1\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"path\":\"/api/users/me\",\"argumentsNames\":[],\"protocol\":\"JAXRS\",\"method\":\"GET\",\"response\":\"com.cassiomolin.example.user.api.model.QueryPersonResult\",\"produces\":\"application/json\",\"consumes\":\"\"},\"responseChain\":[\"javax.ws.rs.core.Response\"],\"annotationsHelper\":[],\"crudToSubject\":[],\"fullname\":\"com_cassiomolin_example_user_api_resource_PersonResource_getAuthenticatedUser\"},{\"name\":\"publicRpcCall\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.user.service.PersonService\",\"namespaceChange\":false,\"annotations\":{\"org.softauto.annotations.ApiForTesting\":{\"protocol\":\"RPC\",\"type\":\"java.lang.String\"},\"class\":{\"org.softauto.annotations.InitializeForTesting\":{\"value\":\"INITIALIZE_EVERY_TIME\",\"type\":\"java.lang.Enum\",\"parameters\":{\"type\":\"String\",\"value\":\"helo\"}},\"javax.enterprise.context.ApplicationScoped\":{}}},\"parametersTypes\":[\"java.lang.String\"],\"id\":29458,\"returnType\":\"java.lang.String\",\"unboxReturnType\":\"java.lang.String\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":true,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[\"name\"],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"protocol\":\"RPC\",\"constructor\":[{\"String\":\"helo\"}],\"classType\":\"INITIALIZE_EVERY_TIME\"},\"responseChain\":[],\"annotationsHelper\":[],\"crudToSubject\":[],\"fullname\":\"com_cassiomolin_example_user_service_PersonService_publicRpcCall\"},{\"name\":\"privateRpcCall\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.user.service.PersonService\",\"namespaceChange\":false,\"annotations\":{\"org.softauto.annotations.ApiForTesting\":{\"protocol\":\"RPC\",\"type\":\"java.lang.String\"},\"class\":{\"org.softauto.annotations.InitializeForTesting\":{\"value\":\"INITIALIZE_EVERY_TIME\",\"type\":\"java.lang.Enum\",\"parameters\":{\"type\":\"String\",\"value\":\"helo\"}},\"javax.enterprise.context.ApplicationScoped\":{}}},\"parametersTypes\":[\"java.lang.String\"],\"id\":29459,\"returnType\":\"java.lang.String\",\"unboxReturnType\":\"java.lang.String\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":true,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[\"name\"],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"protocol\":\"RPC\",\"constructor\":[{\"String\":\"helo\"}],\"classType\":\"INITIALIZE_EVERY_TIME\"},\"responseChain\":[],\"annotationsHelper\":[],\"crudToSubject\":[],\"fullname\":\"com_cassiomolin_example_user_service_PersonService_privateRpcCall\"},{\"name\":\"publicStaticRpcCall\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.user.service.PersonService\",\"namespaceChange\":false,\"annotations\":{\"org.softauto.annotations.ApiForTesting\":{\"protocol\":\"RPC\",\"type\":\"java.lang.String\"},\"class\":{\"org.softauto.annotations.InitializeForTesting\":{\"value\":\"INITIALIZE_EVERY_TIME\",\"type\":\"java.lang.Enum\",\"parameters\":{\"type\":\"String\",\"value\":\"helo\"}},\"javax.enterprise.context.ApplicationScoped\":{}}},\"parametersTypes\":[\"java.lang.String\"],\"id\":29460,\"returnType\":\"java.lang.String\",\"unboxReturnType\":\"java.lang.String\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":true,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[\"name\"],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"protocol\":\"RPC\",\"constructor\":[{\"String\":\"helo\"}],\"classType\":\"INITIALIZE_EVERY_TIME\"},\"responseChain\":[],\"annotationsHelper\":[],\"crudToSubject\":[],\"fullname\":\"com_cassiomolin_example_user_service_PersonService_publicStaticRpcCall\"}]}");
org.softauto.tester.Step com_cassiomolin_example_greeting_api_resource_GreetingResource_getPublicGreeting() throws Exception;

org.softauto.tester.Step com_cassiomolin_example_greeting_api_resource_GreetingResource_getProtectedGreeting() throws Exception;

org.softauto.tester.Step com_cassiomolin_example_greeting_api_resource_GreetingResource_getProtectedGreeting1() throws Exception;

org.softauto.tester.Step com_cassiomolin_example_greeting_service_GreetingService_getPublicGreeting() throws Exception;

org.softauto.tester.Step com_cassiomolin_example_security_jwt_resource_AuthenticationResource_authenticate(com.cassiomolin.example.security.jwt.model.UserCredentials credentials  ) throws Exception;

org.softauto.tester.Step com_cassiomolin_example_security_jwt_resource_AuthenticationResource_refresh() throws Exception;

org.softauto.tester.Step com_cassiomolin_example_user_api_resource_PersonResource_getUsers() throws Exception;

org.softauto.tester.Step com_cassiomolin_example_user_api_resource_PersonResource_getUser(Long userId  ) throws Exception;

org.softauto.tester.Step com_cassiomolin_example_user_api_resource_PersonResource_getAuthenticatedUser() throws Exception;

org.softauto.tester.Step com_cassiomolin_example_user_service_PersonService_publicRpcCall(String name  ) throws Exception;

org.softauto.tester.Step com_cassiomolin_example_user_service_PersonService_privateRpcCall(String name  ) throws Exception;

org.softauto.tester.Step com_cassiomolin_example_user_service_PersonService_publicStaticRpcCall(String name  ) throws Exception;

public interface Callback extends jwtService {
    public static final org.softauto.core.Analyze analyze = jwtService.analyze;
org.softauto.tester.Step  com_cassiomolin_example_greeting_api_resource_GreetingResource_getPublicGreeting(  org.softauto.core.CallFuture<String> callback) throws java.io.IOException;

org.softauto.tester.Step  com_cassiomolin_example_greeting_api_resource_GreetingResource_getProtectedGreeting(  org.softauto.core.CallFuture<String> callback) throws java.io.IOException;

org.softauto.tester.Step  com_cassiomolin_example_greeting_api_resource_GreetingResource_getProtectedGreeting1(  org.softauto.core.CallFuture<String> callback) throws java.io.IOException;

org.softauto.tester.Step  com_cassiomolin_example_greeting_service_GreetingService_getPublicGreeting(  org.softauto.core.CallFuture<String> callback) throws java.io.IOException;

org.softauto.tester.Step  com_cassiomolin_example_security_jwt_resource_AuthenticationResource_authenticate(com.cassiomolin.example.security.jwt.model.UserCredentials credentials   ,org.softauto.core.CallFuture<com.cassiomolin.example.security.jwt.model.AuthenticationToken> callback) throws java.io.IOException;

org.softauto.tester.Step  com_cassiomolin_example_security_jwt_resource_AuthenticationResource_refresh(  org.softauto.core.CallFuture<com.cassiomolin.example.security.jwt.model.AuthenticationToken> callback) throws java.io.IOException;

org.softauto.tester.Step  com_cassiomolin_example_user_api_resource_PersonResource_getUsers(  org.softauto.core.CallFuture<java.util.List> callback) throws java.io.IOException;

org.softauto.tester.Step  com_cassiomolin_example_user_api_resource_PersonResource_getUser(Long userId   , org.softauto.core.CallFuture<com.cassiomolin.example.user.api.model.QueryPersonResult> callback) throws java.io.IOException;

org.softauto.tester.Step  com_cassiomolin_example_user_api_resource_PersonResource_getAuthenticatedUser(  org.softauto.core.CallFuture<com.cassiomolin.example.user.api.model.QueryPersonResult> callback) throws java.io.IOException;

org.softauto.tester.Step  com_cassiomolin_example_user_service_PersonService_publicRpcCall(String name   , org.softauto.core.CallFuture<String> callback) throws java.io.IOException;

org.softauto.tester.Step  com_cassiomolin_example_user_service_PersonService_privateRpcCall(String name   , org.softauto.core.CallFuture<String> callback) throws java.io.IOException;

org.softauto.tester.Step  com_cassiomolin_example_user_service_PersonService_publicStaticRpcCall(String name   , org.softauto.core.CallFuture<String> callback) throws java.io.IOException;


    }
}




