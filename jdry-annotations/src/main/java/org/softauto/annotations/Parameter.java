package org.softauto.annotations;

import org.softauto.annotations.util.DataType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {

    int index() default 0;

    String expression() ;

    String description() default "";

    String type() default "";

    DataType dataType() default DataType.NONE;

    String id() default "";
}
