package org.softauto.annotations;

import org.softauto.annotations.util.Role;
import java.lang.annotation.*;


@Target({ElementType.METHOD,ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiForTesting {

    String description() default "";

    String protocol()  default "RPC";

    Expression[] before() default {};

    Expression[] after() default {};

    String anAssert() default "";

    String id() default "";

    Role role() default Role.NONE;

    Publish[] publish() default {};

    CallBack callback() default @CallBack;

    String returnType() default "";


}
