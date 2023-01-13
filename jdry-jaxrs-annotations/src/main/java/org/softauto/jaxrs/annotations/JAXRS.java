package org.softauto.jaxrs.annotations;


import javax.ws.rs.HttpMethod;
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

    String method() default javax.ws.rs.HttpMethod.GET;

    String produces() default MediaType.APPLICATION_JSON;

    String consumes() default MediaType.APPLICATION_JSON;

    String path() default "";

    Protocol protocol() default Protocol.HTTP;

    Authentication authentication() default @Authentication;

    Mode mode() default Mode.NONE;

    String response() default "";

    //javax.ws.rs.core.Context UriInfo() default @javax.ws.rs.core.Context;

}
