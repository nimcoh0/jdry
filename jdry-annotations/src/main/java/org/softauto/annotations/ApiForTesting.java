package org.softauto.annotations;

import org.softauto.annotations.util.Role;
import org.softauto.annotations.util.StepMode;
import java.lang.annotation.*;


@Target({ElementType.METHOD,ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiForTesting {

    String description() default "";

    String protocol()  ;

    //Class protocol() default RPC.class ;

    String[] dependencies() default {};

    //Assert Assert() default @Assert;

    Before before() default @Before;

    After after() default @After;

    Assert anAssert() default @Assert;

    //Authentication authentication() default @Authentication;

    String id() default "";

    //StepMode mode() default StepMode.API;

    Role role() default Role.NONE;

    String[] group() default {};

    //String[] publish() default {};

    String callback() default "";

    String[] consume() default {};

    //String returnType() default "";

    String scenarioOrder() default "";

    String order() default "";

    String verifyId() default "";

    String listenerId() default "";

    //Expected expected() default @Expected();

    //Result result() default @Result();
}
