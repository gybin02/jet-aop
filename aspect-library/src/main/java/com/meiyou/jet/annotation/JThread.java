package com.jet.jet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 返回值 void 才能使用
 * @author zhengxiaobin
 * @since 17/5/24
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface JThread {
}
