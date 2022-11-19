package org.softauto.annotations;



import org.softauto.annotations.util.ListenerMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface ListenerForTesting {
    String description() default "";

    ListenerMode mode() default ListenerMode.WAIT_FOR_RESULT;

    Mock mock() default @Mock(parameter = {});

    //String result() default "";

    //Before before() default @Before;

    //After after() default @After;

    String type() default "";

    String expression() default "";

    Step step() default @Step(fqmn = "", expression = "", type = "");

}
