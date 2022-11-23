package org.softauto.annotations;

import org.softauto.annotations.util.StepMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiForTesting {

    String description() default "";

    String protocol() default "RPC" ;

    String[] dependencies() default {};

    //Assert Assert() default @Assert;

    Before before() default @Before;

    After after() default @After;

    //Authentication authentication() default @Authentication;

    String id() default "";

    StepMode mode() default StepMode.API;

    String script() default  "";

    String group() default "";
}
