package org.softauto.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface InitializeForTesting {

    ClassType value() default ClassType.INITIALIZE_IF_NOT_EXIST;

    Parameter[] parameters() default {};

}
