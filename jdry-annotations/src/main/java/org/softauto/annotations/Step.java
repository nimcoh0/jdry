package org.softauto.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Step {

    String description() default "";

    String fqmn() default "";

    String expression() default "";

    String type() default "";

    String returnType() default "";

    String callOPtions() default "";

    String protocol() default "RPC" ;

    String id() default "";
}
