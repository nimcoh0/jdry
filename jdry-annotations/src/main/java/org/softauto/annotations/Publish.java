package org.softauto.annotations;

import org.softauto.annotations.util.DataType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Publish {

    String name() default  "";

    String value() default  "";

    DataType dataType() default DataType.NONE;

}
