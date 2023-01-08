package org.softauto.jaxrs.annotations;


import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.METHOD,ElementType.CONSTRUCTOR,ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JAXRS {

    String[] properties() default {};

    String[] headers() default {};

    HttpMethod httpMethod() default HttpMethod.NONE;

    String produce() default MediaType.APPLICATION_JSON;

    String consume() default MediaType.APPLICATION_JSON;

    String path() default "";

    Protocol protocol() default Protocol.HTTP;

    Authentication authentication() default @Authentication;

    Mode mode() default Mode.NONE;

    //javax.ws.rs.core.Context UriInfo() default @javax.ws.rs.core.Context;

}
