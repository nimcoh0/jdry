package org.softauto.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifyForTesting {

    String description() default "";

    //Assert anAssert() default @Assert;

    //Assert Assert() default @Assert;

    //VerifyType verifyType() default VerifyType.RESULT;

    Before before() default @Before;

    After after() default @After;
    //StepForTesting step() default @StepForTesting(fqmn = "", expression = "", type = "", protocol = "");

    //String type() default "";

    //String expression() default "";

    String id() default "";

    //String stepId() default "";

}
