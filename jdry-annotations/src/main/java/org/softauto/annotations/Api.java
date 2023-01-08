package org.softauto.annotations;

import org.softauto.annotations.util.Role;
import org.softauto.annotations.util.StepMode;
import java.lang.annotation.*;


@Target({ElementType.METHOD,ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface Api {

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

    StepMode mode() default StepMode.API;

    Role role() default Role.NONE;

    String[] group() default {};

    Publish[] publish() default @Publish;

    String callback() default "";

    Consume[] consume() default @Consume;
}