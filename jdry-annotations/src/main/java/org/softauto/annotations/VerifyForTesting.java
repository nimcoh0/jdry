package org.softauto.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifyForTesting {

    String description() default "";

    Expression[] after() default {};

    String id() default "";

    String callback() default "";

    String anAssert() default "";

}
