/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package tests.infrastructure;

public interface jwtListenerService {
    public static final org.softauto.core.Analyze analyze = org.softauto.core.Analyze.parse("{\"name\":\"jwt\",\"namespace\":\"tests.infrastructure\",\"steps\":[{\"name\":\"getPublicGreeting\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.greeting.api.resource.GreetingResource\",\"annotations\":{\"Ljavax/ws/rs/GET;\":{},\"Ljavax/ws/rs/Path;\":{\"value\":\"public\",\"type\":\"java.lang.String\"},\"Ljavax/ws/rs/Produces;\":{\"value\":\"text/plain\",\"type\":\"java.lang.String\"},\"Ljavax/annotation/security/PermitAll;\":{},\"class\":{\"Ljavax/enterprise/context/RequestScoped;\":{},\"Ljavax/ws/rs/Path;\":{\"value\":\"greetings\",\"type\":\"java.lang.String\"}}},\"parametersTypes\":[],\"id\":11668,\"returnType\":\"java.lang.String\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"protected\":false,\"public\":true,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"path\":\"/api/greetings/public\",\"argumentsNames\":[],\"protocol\":\"JAXRS\",\"method\":\"GET\",\"response\":\"java.lang.String\",\"produces\":\"text/plain\",\"consumes\":\"\"},\"annotationsHelper\":[],\"fullname\":\"com_cassiomolin_example_greeting_api_resource_GreetingResource_getPublicGreeting\"},{\"name\":\"getProtectedGreeting\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.greeting.api.resource.GreetingResource\",\"annotations\":{\"Ljavax/ws/rs/GET;\":{},\"Ljavax/ws/rs/Path;\":{\"value\":\"protected\",\"type\":\"java.lang.String\"},\"Ljavax/ws/rs/Produces;\":{\"value\":\"text/plain\",\"type\":\"java.lang.String\"},\"class\":{\"Ljavax/enterprise/context/RequestScoped;\":{},\"Ljavax/ws/rs/Path;\":{\"value\":\"greetings\",\"type\":\"java.lang.String\"}}},\"parametersTypes\":[],\"id\":11669,\"returnType\":\"java.lang.String\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"protected\":false,\"public\":true,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"path\":\"/api/greetings/protected\",\"argumentsNames\":[],\"protocol\":\"JAXRS\",\"method\":\"GET\",\"response\":\"java.lang.String\",\"produces\":\"text/plain\",\"consumes\":\"\"},\"annotationsHelper\":[],\"fullname\":\"com_cassiomolin_example_greeting_api_resource_GreetingResource_getProtectedGreeting\"},{\"name\":\"authenticate\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.security.api.resource.AuthenticationResource\",\"annotations\":{\"Ljavax/ws/rs/POST;\":{},\"Ljavax/ws/rs/Consumes;\":{\"value\":\"application/json\",\"type\":\"java.lang.String\"},\"Ljavax/ws/rs/Produces;\":{\"value\":\"application/json\",\"type\":\"java.lang.String\"},\"Ljavax/annotation/security/PermitAll;\":{},\"class\":{\"Ljavax/enterprise/context/RequestScoped;\":{},\"Ljavax/ws/rs/Path;\":{\"value\":\"auth\",\"type\":\"java.lang.String\"}}},\"parametersTypes\":[\"com.cassiomolin.example.security.api.model.UserCredentials\"],\"id\":11922,\"returnType\":\"com.cassiomolin.example.security.api.model.AuthenticationToken\",\"childes\":[{\"name\":\"setToken\",\"type\":\"listener\",\"namespce\":\"com.cassiomolin.example.security.api.model.AuthenticationToken\",\"annotations\":{\"Lorg/softauto/annotations/ListenerForTesting;\":{\"type\":\"BEFORE\"},\"class\":{\"Lorg/softauto/annotations/InitializeForTesting;\":{\"value\":\"INITIALIZE_EVERY_TIME\",\"type\":\"java.lang.Enum\",\"parameters\":{\"type\":\"String\",\"value\":\"helo\"}}}},\"parametersTypes\":[\"java.lang.String\"],\"id\":11915,\"returnType\":\"void\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":true,\"protected\":false,\"public\":true,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[\"token\"],\"modifier\":0,\"constructorParameter\":[],\"properties\":{},\"annotationsHelper\":[]}],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":true,\"protected\":false,\"public\":true,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[\"credentials\"],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"path\":\"/api/auth/\",\"argumentsRequestType\":{\"FormParam\":[{\"index\":0,\"name\":\"credentials\",\"type\":\"com.cassiomolin.example.security.api.model.UserCredentials\"}]},\"argumentsNames\":[\"credentials\"],\"protocol\":\"JAXRS\",\"method\":\"POST\",\"response\":\"com.cassiomolin.example.security.api.model.AuthenticationToken\",\"produces\":\"application/json\",\"consumes\":\"application/json\"},\"annotationsHelper\":[],\"fullname\":\"com_cassiomolin_example_security_api_resource_AuthenticationResource_authenticate\"},{\"name\":\"refresh\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.security.api.resource.AuthenticationResource\",\"annotations\":{\"Ljavax/ws/rs/POST;\":{},\"Ljavax/ws/rs/Path;\":{\"value\":\"refresh\",\"type\":\"java.lang.String\"},\"Ljavax/ws/rs/Produces;\":{\"value\":\"application/json\",\"type\":\"java.lang.String\"},\"class\":{\"Ljavax/enterprise/context/RequestScoped;\":{},\"Ljavax/ws/rs/Path;\":{\"value\":\"auth\",\"type\":\"java.lang.String\"}}},\"parametersTypes\":[],\"id\":11923,\"returnType\":\"java.lang.String\",\"childes\":[{\"name\":\"setToken\",\"type\":\"listener\",\"namespce\":\"com.cassiomolin.example.security.api.model.AuthenticationToken\",\"annotations\":{\"Lorg/softauto/annotations/ListenerForTesting;\":{\"type\":\"BEFORE\"},\"class\":{\"Lorg/softauto/annotations/InitializeForTesting;\":{\"value\":\"INITIALIZE_EVERY_TIME\",\"type\":\"java.lang.Enum\",\"parameters\":{\"type\":\"String\",\"value\":\"helo\"}}}},\"parametersTypes\":[\"java.lang.String\"],\"id\":11915,\"returnType\":\"void\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"protected\":false,\"public\":true,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[\"token\"],\"modifier\":0,\"constructorParameter\":[],\"properties\":{},\"annotationsHelper\":[]}],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"protected\":false,\"public\":true,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"path\":\"/api/auth/refresh\",\"argumentsNames\":[],\"protocol\":\"JAXRS\",\"method\":\"POST\",\"response\":\"java.lang.String\",\"produces\":\"application/json\",\"consumes\":\"\"},\"annotationsHelper\":[],\"fullname\":\"com_cassiomolin_example_security_api_resource_AuthenticationResource_refresh\"},{\"name\":\"getUsers\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.user.api.resource.PersonResource\",\"annotations\":{\"Ljavax/ws/rs/GET;\":{},\"Ljavax/ws/rs/Produces;\":{\"value\":\"application/json\",\"type\":\"java.lang.String\"},\"Ljavax/annotation/security/RolesAllowed;\":{\"value\":\"ADMIN\",\"type\":\"java.lang.String\"},\"class\":{\"Ljavax/enterprise/context/RequestScoped;\":{},\"Ljavax/ws/rs/Path;\":{\"value\":\"users\",\"type\":\"java.lang.String\"}}},\"parametersTypes\":[],\"id\":11945,\"returnType\":\"java.util.List\",\"childes\":[{\"name\":\"findAll\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.user.service.PersonService\",\"annotations\":{\"Lorg/softauto/annotations/ApiForTesting;\":{},\"class\":{\"Ljavax/enterprise/context/ApplicationScoped;\":{}}},\"parametersTypes\":[],\"id\":11721,\"returnType\":\"java.util.List\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"protected\":false,\"public\":true,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[],\"modifier\":0,\"constructorParameter\":[],\"properties\":{},\"annotationsHelper\":[]}],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"protected\":false,\"public\":true,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"path\":\"/api/users/\",\"argumentsNames\":[],\"protocol\":\"JAXRS\",\"method\":\"GET\",\"response\":\"java.util.List\",\"produces\":\"application/json\",\"consumes\":\"\"},\"annotationsHelper\":[],\"fullname\":\"com_cassiomolin_example_user_api_resource_PersonResource_getUsers\"},{\"name\":\"getUser\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.user.api.resource.PersonResource\",\"annotations\":{\"Ljavax/ws/rs/GET;\":{},\"Ljavax/ws/rs/Path;\":{\"value\":\"{userId}\",\"type\":\"java.lang.String\"},\"Ljavax/ws/rs/Produces;\":{\"value\":\"application/json\",\"type\":\"java.lang.String\"},\"Ljavax/annotation/security/RolesAllowed;\":{\"value\":\"ADMIN\",\"type\":\"java.lang.String\"},\"Ljavax/ws/rs/PathParam;\":{\"value\":\"userId\",\"type\":\"java.lang.String\",\"index\":0},\"class\":{\"Ljavax/enterprise/context/RequestScoped;\":{},\"Ljavax/ws/rs/Path;\":{\"value\":\"users\",\"type\":\"java.lang.String\"}}},\"parametersTypes\":[\"java.lang.Long\"],\"id\":11946,\"returnType\":\"com.cassiomolin.example.user.api.model.QueryPersonResult\",\"childes\":[{\"name\":\"findById\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.user.service.PersonService\",\"annotations\":{\"Lorg/softauto/annotations/ApiForTesting;\":{},\"class\":{\"Ljavax/enterprise/context/ApplicationScoped;\":{}}},\"parametersTypes\":[\"java.lang.Long\"],\"id\":11722,\"returnType\":\"java.util.Optional\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":true,\"protected\":false,\"public\":true,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[\"userId\"],\"modifier\":0,\"constructorParameter\":[],\"properties\":{},\"annotationsHelper\":[]}],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":true,\"protected\":false,\"public\":true,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[\"userId\"],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"path\":\"/api/users/{userId}\",\"argumentsRequestType\":{\"PathParam\":[{\"index\":0,\"name\":\"userId\",\"type\":\"java.lang.Long\"}]},\"argumentsNames\":[\"userId\"],\"protocol\":\"JAXRS\",\"method\":\"GET\",\"response\":\"com.cassiomolin.example.user.api.model.QueryPersonResult\",\"produces\":\"application/json\",\"consumes\":\"\"},\"annotationsHelper\":[],\"fullname\":\"com_cassiomolin_example_user_api_resource_PersonResource_getUser\"},{\"name\":\"getAuthenticatedUser\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.user.api.resource.PersonResource\",\"annotations\":{\"Ljavax/ws/rs/GET;\":{},\"Ljavax/ws/rs/Path;\":{\"value\":\"me\",\"type\":\"java.lang.String\"},\"Ljavax/ws/rs/Produces;\":{\"value\":\"application/json\",\"type\":\"java.lang.String\"},\"Ljavax/annotation/security/PermitAll;\":{},\"class\":{\"Ljavax/enterprise/context/RequestScoped;\":{},\"Ljavax/ws/rs/Path;\":{\"value\":\"users\",\"type\":\"java.lang.String\"}}},\"parametersTypes\":[],\"id\":11947,\"returnType\":\"java.util.HashSet\",\"childes\":[{\"name\":\"findByUsernameOrEmail\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.user.service.PersonService\",\"annotations\":{\"Lorg/softauto/annotations/ApiForTesting;\":{},\"class\":{\"Ljavax/enterprise/context/ApplicationScoped;\":{}}},\"parametersTypes\":[\"java.lang.String\"],\"id\":11720,\"returnType\":\"com.cassiomolin.example.user.domain.Person\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"protected\":false,\"public\":true,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[\"identifier\"],\"modifier\":0,\"constructorParameter\":[],\"properties\":{},\"annotationsHelper\":[]}],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"protected\":false,\"public\":true,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[],\"modifier\":0,\"constructorParameter\":[],\"properties\":{\"path\":\"/api/users/me\",\"argumentsNames\":[],\"protocol\":\"JAXRS\",\"method\":\"GET\",\"response\":\"java.util.HashSet\",\"produces\":\"application/json\",\"consumes\":\"\"},\"annotationsHelper\":[],\"fullname\":\"com_cassiomolin_example_user_api_resource_PersonResource_getAuthenticatedUser\"},{\"name\":\"findByUsernameOrEmail\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.user.service.PersonService\",\"annotations\":{\"Lorg/softauto/annotations/ApiForTesting;\":{},\"class\":{\"Ljavax/enterprise/context/ApplicationScoped;\":{}}},\"parametersTypes\":[\"java.lang.String\"],\"id\":11720,\"returnType\":\"com.cassiomolin.example.user.domain.Person\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":true,\"protected\":false,\"public\":true,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[\"identifier\"],\"modifier\":0,\"constructorParameter\":[],\"properties\":{},\"annotationsHelper\":[],\"fullname\":\"com_cassiomolin_example_user_service_PersonService_findByUsernameOrEmail\"},{\"name\":\"findAll\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.user.service.PersonService\",\"annotations\":{\"Lorg/softauto/annotations/ApiForTesting;\":{},\"class\":{\"Ljavax/enterprise/context/ApplicationScoped;\":{}}},\"parametersTypes\":[],\"id\":11721,\"returnType\":\"java.util.List\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":false,\"protected\":false,\"public\":true,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[],\"modifier\":0,\"constructorParameter\":[],\"properties\":{},\"annotationsHelper\":[],\"fullname\":\"com_cassiomolin_example_user_service_PersonService_findAll\"},{\"name\":\"findById\",\"type\":\"method\",\"namespce\":\"com.cassiomolin.example.user.service.PersonService\",\"annotations\":{\"Lorg/softauto/annotations/ApiForTesting;\":{},\"class\":{\"Ljavax/enterprise/context/ApplicationScoped;\":{}}},\"parametersTypes\":[\"java.lang.Long\"],\"id\":11722,\"returnType\":\"java.util.Optional\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":true,\"protected\":false,\"public\":true,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[\"userId\"],\"modifier\":0,\"constructorParameter\":[],\"properties\":{},\"annotationsHelper\":[],\"fullname\":\"com_cassiomolin_example_user_service_PersonService_findById\"}],\"listeners\":[{\"name\":\"setToken\",\"type\":\"listener\",\"namespce\":\"com.cassiomolin.example.security.api.model.AuthenticationToken\",\"annotations\":{\"Lorg/softauto/annotations/ListenerForTesting;\":{\"type\":\"BEFORE\"},\"class\":{\"Lorg/softauto/annotations/InitializeForTesting;\":{\"value\":\"INITIALIZE_EVERY_TIME\",\"type\":\"java.lang.Enum\",\"parameters\":{\"type\":\"String\",\"value\":\"helo\"}}}},\"parametersTypes\":[\"java.lang.String\"],\"id\":11915,\"returnType\":\"void\",\"childes\":[],\"classInfo\":{\"private\":false,\"static\":false,\"innerClass\":false,\"abstract\":false,\"interface\":false,\"enum\":false,\"hasParameters\":true,\"protected\":false,\"public\":true,\"applicationClass\":true,\"libraryClass\":false,\"final\":false,\"entity\":false,\"javaLibraryClass\":false},\"argumentsNames\":[\"token\"],\"modifier\":0,\"constructorParameter\":[{\"String\":\"helo\"}],\"classType\":\"INITIALIZE_EVERY_TIME\",\"properties\":{},\"annotationsHelper\":[],\"fullname\":\"com_cassiomolin_example_security_api_model_AuthenticationToken_setToken\"}]}");
org.softauto.tester.Listener com_cassiomolin_example_security_api_model_AuthenticationToken_setToken() throws Exception;
}