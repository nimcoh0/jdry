package org.softauto.annotations;


import org.softauto.annotations.util.VerifyType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifyForTesting {

    String description() default "";

    //Assert Assert() default @Assert;

    //VerifyType verifyType() default VerifyType.RESULT;

    //Before before() default @Before;

    //After after() default @After;
    Step step() default @Step(fqmn = "", expression = "", type = "");

    String type() default "";

    String expression() default "";

}
