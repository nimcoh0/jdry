package org.softauto.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @// TODO: 15/11/2022 add support for UpdateForTesting 
 */
@Target({ElementType.FIELD,ElementType.LOCAL_VARIABLE,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateForTesting {

    String value() default "";


}
