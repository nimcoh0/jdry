package org.softauto.annotations;

import java.lang.annotation.*;


@Target({ElementType.METHOD,ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiForTesting {


}
