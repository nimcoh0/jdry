package org.softauto.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE,ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface Assert {

    String assertType() default "AssertEquals";

    //Before before() default @Before;

    //After after() default @After;

    String description() default "";

    String expression() default "";

    Step step() default @Step;

    String type() default "";

    String call() default "";
}
