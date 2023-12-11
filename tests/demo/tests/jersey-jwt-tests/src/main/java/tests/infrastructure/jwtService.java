/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package tests.infrastructure;

public interface jwtService {
    public static final org.softauto.core.Analyze analyze = org.softauto.core.Analyze.parse("{\"name\":\"jwt\",\"namespace\":\"tests.infrastructure\",\"steps\":[{\"name\":\"getPublicGreeting\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.greeting.api.resource.GreetingResource\",\"annotations\":{\"Ljavax/ws/rs/GET;\":{},\"Ljavax/ws/rs/Path;\":{\"value\":\"public\",\"type\":\"java.lang.String\"},\"Ljavax/ws/rs/Produces;\":{\"value\":\"text/plain\",\"type\":\"java.lang.String\"},\"Ljavax/annotation/security/PermitAll;\":{},\"class\":{\"Ljavax/enterprise/context/RequestScoped;\":{},\"Ljavax/ws/rs/Path;\":{\"value\":\"greetings\",\"type\":\"java.lang.String\"}}},\"parametersTypes\":[],\"id\":16874,\"returnType\":\"java.lang.String\",\"childes\":[{\"name\":\"getPublicGreeting\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.greeting.service.GreetingService\",\"annotations\":{\"Lorg/softauto/annotations/ApiForTesting;\":{\"protocol\":\"RPC\",\"type\":\"java.lang.String\"},\"class\":{\"Ljavax/enterprise/context/ApplicationScoped;\":{}}},\"parametersTypes\":[],\"id\":16877,\"returnType\":\"java.lang.String\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[],\"modifier\":0,\"constructorParameter\":[],\"properties\":{},\"annotationsHelper\":[]}],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"path\":\"/api/greetings/public\",\"argumentsNames\":[],\"protocol\":\"JAXRS\",\"method\":\"GET\",\"response\":\"java.lang.String\",\"produces\":\"text/plain\",\"consumes\":\"\"},\"annotationsHelper\":[],\"fullname\":\"com_cassiomolin_example_greeting_api_resource_GreetingResource_getPublicGreeting\"},{\"name\":\"getProtectedGreeting\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.greeting.api.resource.GreetingResource\",\"annotations\":{\"Ljavax/ws/rs/GET;\":{},\"Ljavax/ws/rs/Path;\":{\"value\":\"protected\",\"type\":\"java.lang.String\"},\"Ljavax/ws/rs/Produces;\":{\"value\":\"text/plain\",\"type\":\"java.lang.String\"},\"class\":{\"Ljavax/enterprise/context/RequestScoped;\":{},\"Ljavax/ws/rs/Path;\":{\"value\":\"greetings\",\"type\":\"java.lang.String\"}}},\"parametersTypes\":[],\"id\":16875,\"returnType\":\"java.lang.String\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"path\":\"/api/greetings/protected\",\"argumentsNames\":[],\"protocol\":\"JAXRS\",\"method\":\"GET\",\"response\":\"java.lang.String\",\"produces\":\"text/plain\",\"consumes\":\"\"},\"annotationsHelper\":[],\"fullname\":\"com_cassiomolin_example_greeting_api_resource_GreetingResource_getProtectedGreeting\"},{\"name\":\"getPublicGreeting\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.greeting.service.GreetingService\",\"annotations\":{\"Lorg/softauto/annotations/ApiForTesting;\":{\"protocol\":\"RPC\",\"type\":\"java.lang.String\"},\"class\":{\"Ljavax/enterprise/context/ApplicationScoped;\":{}}},\"parametersTypes\":[],\"id\":16877,\"returnType\":\"java.lang.String\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"protocol\":\"RPC\"},\"annotationsHelper\":[],\"fullname\":\"com_cassiomolin_example_greeting_service_GreetingService_getPublicGreeting\"},{\"name\":\"authenticate\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.security.jwt.resource.AuthenticationResource\",\"annotations\":{\"Ljavax/ws/rs/POST;\":{},\"Ljavax/ws/rs/Consumes;\":{\"value\":\"application/json\",\"type\":\"java.lang.String\"},\"Ljavax/ws/rs/Produces;\":{\"value\":\"application/json\",\"type\":\"java.lang.String\"},\"Ljavax/annotation/security/PermitAll;\":{},\"class\":{\"Ljavax/enterprise/context/RequestScoped;\":{},\"Ljavax/ws/rs/Path;\":{\"value\":\"auth\",\"type\":\"java.lang.String\"}}},\"parametersTypes\":[\"com.cassiomolin.example.security.jwt.model.UserCredentials\"],\"id\":17036,\"returnType\":\"com.cassiomolin.example.security.jwt.model.AuthenticationToken\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":true,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[\"credentials\"],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"path\":\"/api/auth/\",\"argumentsRequestType\":{\"FormParam\":[{\"index\":0,\"name\":\"credentials\",\"type\":\"com.cassiomolin.example.security.jwt.model.UserCredentials\"}]},\"argumentsNames\":[\"credentials\"],\"protocol\":\"JAXRS\",\"method\":\"POST\",\"response\":\"com.cassiomolin.example.security.jwt.model.AuthenticationToken\",\"produces\":\"application/json\",\"consumes\":\"application/json\"},\"annotationsHelper\":[],\"fullname\":\"com_cassiomolin_example_security_jwt_resource_AuthenticationResource_authenticate\"},{\"name\":\"refresh\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.security.jwt.resource.AuthenticationResource\",\"annotations\":{\"Ljavax/ws/rs/POST;\":{},\"Ljavax/ws/rs/Path;\":{\"value\":\"refresh\",\"type\":\"java.lang.String\"},\"Ljavax/ws/rs/Produces;\":{\"value\":\"application/json\",\"type\":\"java.lang.String\"},\"class\":{\"Ljavax/enterprise/context/RequestScoped;\":{},\"Ljavax/ws/rs/Path;\":{\"value\":\"auth\",\"type\":\"java.lang.String\"}}},\"parametersTypes\":[],\"id\":17037,\"returnType\":\"com.cassiomolin.example.security.jwt.model.AuthenticationToken\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"path\":\"/api/auth/refresh\",\"argumentsNames\":[],\"protocol\":\"JAXRS\",\"method\":\"POST\",\"response\":\"com.cassiomolin.example.security.jwt.model.AuthenticationToken\",\"produces\":\"application/json\",\"consumes\":\"\"},\"annotationsHelper\":[],\"fullname\":\"com_cassiomolin_example_security_jwt_resource_AuthenticationResource_refresh\"},{\"name\":\"getUsers\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.user.api.resource.PersonResource\",\"annotations\":{\"Ljavax/ws/rs/GET;\":{},\"Ljavax/ws/rs/Produces;\":{\"value\":\"application/json\",\"type\":\"java.lang.String\"},\"Ljavax/annotation/security/RolesAllowed;\":{\"value\":\"ADMIN\",\"type\":\"java.lang.String\"},\"class\":{\"Ljavax/enterprise/context/RequestScoped;\":{},\"Ljavax/ws/rs/Path;\":{\"value\":\"users\",\"type\":\"java.lang.String\"}}},\"parametersTypes\":[],\"id\":17059,\"returnType\":\"java.util.List\",\"childes\":[{\"name\":\"findAll\",\"type\":\"listener\",\"namespce\":\"com.cassiomolin.example.user.service.PersonService\",\"annotations\":{\"Lorg/softauto/annotations/ListenerForTesting;\":{\"type\":\"AFTER\"},\"class\":{\"Lorg/softauto/annotations/InitializeForTesting;\":{\"value\":\"INITIALIZE_EVERY_TIME\",\"type\":\"java.lang.Enum\",\"parameters\":{\"type\":\"String\",\"value\":\"helo\"}},\"Ljavax/enterprise/context/ApplicationScoped;\":{}}},\"parametersTypes\":[],\"id\":16888,\"returnType\":\"java.util.List\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[],\"modifier\":0,\"constructorParameter\":[],\"properties\":{},\"annotationsHelper\":[]}],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"path\":\"/api/users/\",\"argumentsNames\":[],\"protocol\":\"JAXRS\",\"method\":\"GET\",\"response\":\"java.util.List\",\"produces\":\"application/json\",\"consumes\":\"\"},\"annotationsHelper\":[],\"fullname\":\"com_cassiomolin_example_user_api_resource_PersonResource_getUsers\"},{\"name\":\"getUser\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.user.api.resource.PersonResource\",\"annotations\":{\"Ljavax/ws/rs/GET;\":{},\"Ljavax/ws/rs/Path;\":{\"value\":\"{userId}\",\"type\":\"java.lang.String\"},\"Ljavax/ws/rs/Produces;\":{\"value\":\"application/json\",\"type\":\"java.lang.String\"},\"Ljavax/annotation/security/RolesAllowed;\":{\"value\":\"ADMIN\",\"type\":\"java.lang.String\"},\"Ljavax/ws/rs/PathParam;\":{\"value\":\"userId\",\"type\":\"java.lang.String\",\"index\":0},\"class\":{\"Ljavax/enterprise/context/RequestScoped;\":{},\"Ljavax/ws/rs/Path;\":{\"value\":\"users\",\"type\":\"java.lang.String\"}}},\"parametersTypes\":[\"java.lang.Long\"],\"id\":17060,\"returnType\":\"com.cassiomolin.example.user.api.model.QueryPersonResult\",\"childes\":[{\"name\":\"findById\",\"type\":\"listener\",\"namespce\":\"com.cassiomolin.example.user.service.PersonService\",\"annotations\":{\"Lorg/softauto/annotations/ListenerForTesting;\":{\"type\":\"BEFORE\"},\"class\":{\"Lorg/softauto/annotations/InitializeForTesting;\":{\"value\":\"INITIALIZE_EVERY_TIME\",\"type\":\"java.lang.Enum\",\"parameters\":{\"type\":\"String\",\"value\":\"helo\"}},\"Ljavax/enterprise/context/ApplicationScoped;\":{}}},\"parametersTypes\":[\"java.lang.Long\"],\"id\":16889,\"returnType\":\"java.util.Optional\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":true,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[\"userId\"],\"modifier\":0,\"constructorParameter\":[],\"properties\":{},\"annotationsHelper\":[]}],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":true,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[\"userId\"],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"path\":\"/api/users/{userId}\",\"argumentsRequestType\":{\"PathParam\":[{\"index\":0,\"name\":\"userId\",\"type\":\"java.lang.Long\"}]},\"argumentsNames\":[\"userId\"],\"protocol\":\"JAXRS\",\"method\":\"GET\",\"response\":\"com.cassiomolin.example.user.api.model.QueryPersonResult\",\"produces\":\"application/json\",\"consumes\":\"\"},\"annotationsHelper\":[],\"fullname\":\"com_cassiomolin_example_user_api_resource_PersonResource_getUser\"},{\"name\":\"getAuthenticatedUser\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.user.api.resource.PersonResource\",\"annotations\":{\"Ljavax/ws/rs/GET;\":{},\"Ljavax/ws/rs/Path;\":{\"value\":\"me\",\"type\":\"java.lang.String\"},\"Ljavax/ws/rs/Produces;\":{\"value\":\"application/json\",\"type\":\"java.lang.String\"},\"Ljavax/annotation/security/PermitAll;\":{},\"class\":{\"Ljavax/enterprise/context/RequestScoped;\":{},\"Ljavax/ws/rs/Path;\":{\"value\":\"users\",\"type\":\"java.lang.String\"}}},\"parametersTypes\":[],\"id\":17061,\"returnType\":\"java.util.HashSet\",\"childes\":[{\"name\":\"findByUsernameOrEmail\",\"type\":\"listener\",\"namespce\":\"com.cassiomolin.example.user.service.PersonService\",\"annotations\":{\"Lorg/softauto/annotations/ListenerForTesting;\":{\"type\":\"BEFORE\"},\"class\":{\"Lorg/softauto/annotations/InitializeForTesting;\":{\"value\":\"INITIALIZE_EVERY_TIME\",\"type\":\"java.lang.Enum\",\"parameters\":{\"type\":\"String\",\"value\":\"helo\"}},\"Ljavax/enterprise/context/ApplicationScoped;\":{}}},\"parametersTypes\":[\"java.lang.String\"],\"id\":16887,\"returnType\":\"com.cassiomolin.example.user.domain.Person\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[\"identifier\"],\"modifier\":0,\"constructorParameter\":[],\"properties\":{},\"annotationsHelper\":[]}],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"path\":\"/api/users/me\",\"argumentsNames\":[],\"protocol\":\"JAXRS\",\"method\":\"GET\",\"response\":\"java.util.HashSet\",\"produces\":\"application/json\",\"consumes\":\"\"},\"annotationsHelper\":[],\"fullname\":\"com_cassiomolin_example_user_api_resource_PersonResource_getAuthenticatedUser\"},{\"name\":\"publicRpcCall\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.user.service.PersonService\",\"annotations\":{\"Lorg/softauto/annotations/ApiForTesting;\":{\"protocol\":\"RPC\",\"type\":\"java.lang.String\"},\"class\":{\"Lorg/softauto/annotations/InitializeForTesting;\":{\"value\":\"INITIALIZE_EVERY_TIME\",\"type\":\"java.lang.Enum\",\"parameters\":{\"type\":\"String\",\"value\":\"helo\"}},\"Ljavax/enterprise/context/ApplicationScoped;\":{}}},\"parametersTypes\":[\"java.lang.String\"],\"id\":16884,\"returnType\":\"java.lang.String\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":true,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[\"name\"],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"protocol\":\"RPC\",\"constructor\":[{\"String\":\"helo\"}],\"classType\":\"INITIALIZE_EVERY_TIME\"},\"annotationsHelper\":[],\"fullname\":\"com_cassiomolin_example_user_service_PersonService_publicRpcCall\"},{\"name\":\"privateRpcCall\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.user.service.PersonService\",\"annotations\":{\"Lorg/softauto/annotations/ApiForTesting;\":{\"protocol\":\"RPC\",\"type\":\"java.lang.String\"},\"class\":{\"Lorg/softauto/annotations/InitializeForTesting;\":{\"value\":\"INITIALIZE_EVERY_TIME\",\"type\":\"java.lang.Enum\",\"parameters\":{\"type\":\"String\",\"value\":\"helo\"}},\"Ljavax/enterprise/context/ApplicationScoped;\":{}}},\"parametersTypes\":[\"java.lang.String\"],\"id\":16885,\"returnType\":\"java.lang.String\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":true,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[\"name\"],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"protocol\":\"RPC\",\"constructor\":[{\"String\":\"helo\"}],\"classType\":\"INITIALIZE_EVERY_TIME\"},\"annotationsHelper\":[],\"fullname\":\"com_cassiomolin_example_user_service_PersonService_privateRpcCall\"},{\"name\":\"publicStaticRpcCall\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.user.service.PersonService\",\"annotations\":{\"Lorg/softauto/annotations/ApiForTesting;\":{\"protocol\":\"RPC\",\"type\":\"java.lang.String\"},\"class\":{\"Lorg/softauto/annotations/InitializeForTesting;\":{\"value\":\"INITIALIZE_EVERY_TIME\",\"type\":\"java.lang.Enum\",\"parameters\":{\"type\":\"String\",\"value\":\"helo\"}},\"Ljavax/enterprise/context/ApplicationScoped;\":{}}},\"parametersTypes\":[\"java.lang.String\"],\"id\":16886,\"returnType\":\"java.lang.String\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":true,\"public\":true,\"protected\":false,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[\"name\"],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"protocol\":\"RPC\",\"constructor\":[{\"String\":\"helo\"}],\"classType\":\"INITIALIZE_EVERY_TIME\"},\"annotationsHelper\":[],\"fullname\":\"com_cassiomolin_example_user_service_PersonService_publicStaticRpcCall\"}]}");
org.softauto.tester.Step com_cassiomolin_example_greeting_api_resource_GreetingResource_getPublicGreeting() throws Exception;

org.softauto.tester.Step com_cassiomolin_example_greeting_api_resource_GreetingResource_getProtectedGreeting() throws Exception;

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
org.softauto.tester.Step  com_cassiomolin_example_greeting_api_resource_GreetingResource_getPublicGreeting(  org.apache.avro.ipc.CallFuture<String> callback) throws java.io.IOException;

org.softauto.tester.Step  com_cassiomolin_example_greeting_api_resource_GreetingResource_getProtectedGreeting(  org.apache.avro.ipc.CallFuture<String> callback) throws java.io.IOException;

org.softauto.tester.Step  com_cassiomolin_example_greeting_service_GreetingService_getPublicGreeting(  org.apache.avro.ipc.CallFuture<String> callback) throws java.io.IOException;

org.softauto.tester.Step  com_cassiomolin_example_security_jwt_resource_AuthenticationResource_authenticate(com.cassiomolin.example.security.jwt.model.UserCredentials credentials   ,org.apache.avro.ipc.CallFuture<com.cassiomolin.example.security.jwt.model.AuthenticationToken> callback) throws java.io.IOException;

org.softauto.tester.Step  com_cassiomolin_example_security_jwt_resource_AuthenticationResource_refresh(  org.apache.avro.ipc.CallFuture<com.cassiomolin.example.security.jwt.model.AuthenticationToken> callback) throws java.io.IOException;

org.softauto.tester.Step  com_cassiomolin_example_user_api_resource_PersonResource_getUsers(  org.apache.avro.ipc.CallFuture<java.util.List> callback) throws java.io.IOException;

org.softauto.tester.Step  com_cassiomolin_example_user_api_resource_PersonResource_getUser(Long userId   , org.apache.avro.ipc.CallFuture<com.cassiomolin.example.user.api.model.QueryPersonResult> callback) throws java.io.IOException;

org.softauto.tester.Step  com_cassiomolin_example_user_api_resource_PersonResource_getAuthenticatedUser(  org.apache.avro.ipc.CallFuture<java.util.HashSet> callback) throws java.io.IOException;

org.softauto.tester.Step  com_cassiomolin_example_user_service_PersonService_publicRpcCall(String name   , org.apache.avro.ipc.CallFuture<String> callback) throws java.io.IOException;

org.softauto.tester.Step  com_cassiomolin_example_user_service_PersonService_privateRpcCall(String name   , org.apache.avro.ipc.CallFuture<String> callback) throws java.io.IOException;

org.softauto.tester.Step  com_cassiomolin_example_user_service_PersonService_publicStaticRpcCall(String name   , org.apache.avro.ipc.CallFuture<String> callback) throws java.io.IOException;


    }
}




