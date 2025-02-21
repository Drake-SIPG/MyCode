package com.sse.sseapp.app.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 加密
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Decrypt {
}
