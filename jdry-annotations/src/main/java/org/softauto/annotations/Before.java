package org.softauto.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Before {

    String expression() default "";

    Step step() default @Step(fqmn = "", expression = "", type = "") ;

    String description() default "";

    String type() default "";

    Parameter[] parameter() default {};
}
