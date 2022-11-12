package org.softauto.jaxrs.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Authentication {

    AuthenticationType Schema() default AuthenticationType.NONE;

    String username() default "";

    String password() default "";

    boolean session() default false;


}
