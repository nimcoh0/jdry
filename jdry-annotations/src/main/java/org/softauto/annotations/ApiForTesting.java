package org.softauto.annotations;

import org.softauto.annotations.util.Role;
import org.softauto.annotations.util.StepMode;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiForTesting {

    String description() default "";

    String protocol() default "RPC" ;

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

    String group() default "";


}
