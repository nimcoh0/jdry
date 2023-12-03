package org.softauto.logger;

import io.netty.handler.logging.LogLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.CONSTRUCTOR})
public @interface Log {

    LogLevel value() default LogLevel.INFO;

    ChronoUnit unit() default ChronoUnit.SECONDS;

    boolean showArgs() default false;

    boolean showResult() default false;

    boolean showExecutionTime() default true;
}
