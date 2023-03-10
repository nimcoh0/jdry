package org.softauto.annotations;

public @interface Expected {

    String value() default "";

    String type() default "";
}
