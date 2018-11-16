package com.meiyou.jet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhengxiaobin
 * @since 17/5/25
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface JPermission {
}
