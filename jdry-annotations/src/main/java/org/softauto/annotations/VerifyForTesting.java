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

    String before() default "";

    String after() default "";
    //StepForTesting step() default @StepForTesting(fqmn = "", expression = "", type = "", protocol = "");

    //String type() default "";

    //String expression() default "";

    String id() default "";

    String callback() default "";

    //String stepId() default "";

}
