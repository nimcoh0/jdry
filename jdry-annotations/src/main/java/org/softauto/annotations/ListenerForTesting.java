package org.softauto.annotations;



import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface ListenerForTesting {
    String description() default "";

    ListenerMode mode() default ListenerMode.NONE;

    Mock mock() default @Mock;

    String result() default "";

    Before before() default @Before;

    After after() default @After;

}
