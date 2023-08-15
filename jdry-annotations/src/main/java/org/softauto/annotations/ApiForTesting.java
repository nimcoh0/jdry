package org.softauto.annotations;

import org.softauto.annotations.util.Policy;
import org.softauto.annotations.util.Role;
import org.softauto.annotations.util.StepMode;
import java.lang.annotation.*;
import java.lang.reflect.Method;


@Target({ElementType.METHOD,ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiForTesting {

    String description() default "";

    String protocol()  default "RPC";

    //Class protocol() default RPC.class ;

    String[] dependencies() default {};

    //Assert Assert() default @Assert;

    Expression[] before() default {};

    Expression[] after() default {};

    String anAssert() default "";

    String[] groups() default {};

    //String assertType() default "AssertEquals";

    //Authentication authentication() default @Authentication;

    String id() default "";

    //StepMode mode() default StepMode.API;

    Role role() default Role.NONE;

    //String[] group() default {};

    Publish[] publish() default {};

    String callback() default "";

    //String[] consume() default {};

    String returnType() default "";

    //String scenarioOrder() default "";

    //String order() default "";

    String verifyId() default "";

    Policy policy() default Policy.ENABLE_UPDATE;

    String expected() default "";

    Credentials credentials() default @Credentials;

    //String tokenInfo() default "";

    //String[] listenerId() default {};

    //Expected expected() default @Expected();

    //Result result() default @Result();
}
