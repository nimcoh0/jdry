package org.softauto.annotations;

import org.softauto.annotations.util.StepMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StepForTesting {

    String description() default "";

    String protocol() default "RPC" ;

    String[] dependencies() default {};

    //Assert Assert() default @Assert;

    Before before() default @Before(expression = "", type = "");

    After after() default @After(expression = "", type = "");

    //Authentication authentication() default @Authentication;

    String id() default "";

    StepMode mode() default StepMode.API;

    String script() default  "";

    String group() default "";
}
