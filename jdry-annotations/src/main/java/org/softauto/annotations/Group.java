package org.softauto.annotations;

public @interface Group {

    String id() default "";

    int order() default -1;


}
