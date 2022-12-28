package org.softauto.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface After {

    String value() default "";

    Step step() default @Step;

    String type() default "";

    String description() default "";

}
