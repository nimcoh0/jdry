package org.softauto.annotations;

public @interface Result {

    String value() default "";

    String type() default "";
}
