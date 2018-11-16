package com.jet.jet.aspect;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 根据注解TimeLog自动添加打印方法耗代码，通过aop切片的方式在编译期间织入源代码中
 * 功能：自动打印方法的耗时
 */
@Aspect
public class LogTimeAspect {
    private static final String TAG = "LogTimeAspect";

    @Pointcut("execution(@com.jet.jet.annotation.JLogTime * *(..))")//方法切入点
    public void methodAnnotated() {
    }

    @Pointcut("execution(@com.jet.jet.annotation.JLogTime *.new(..))")//构造器切入点
    public void constructorAnnotated() {
    }

    @Around("methodAnnotated() || constructorAnnotated()")//在连接点进行方法替换
    public Object aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();//执行原方法
        long time = System.currentTimeMillis() - startTime;
        String content = className + "." + methodName + " 执行时间： " + "[" + time + "ms]";
        Log.w(TAG, content);// 打印时间差
        return result;
    }
}