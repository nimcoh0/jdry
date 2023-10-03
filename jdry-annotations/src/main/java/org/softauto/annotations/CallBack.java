package org.softauto.annotations;

import org.softauto.annotations.callback.OnFailure;
import org.softauto.annotations.callback.OnSuccess;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CallBack {

        String name() default "";

        String type() default "";

        boolean enabledAssert() default  false;

        OnFailure onFailure() default @OnFailure;

        OnSuccess onSuccess() default @OnSuccess;
}
