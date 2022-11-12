package org.softauto.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifyForTesting {

    String description() default "";

    //Assert Assert() default @Assert;

    VerifyType type() default VerifyType.RESULT;

    Before before() default @Before;

    After after() default @After;


}
