package com.meiyou.jet.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 可以安全地执行方法,而无需考虑是否会抛出运行时异常
 */
@Aspect
public class TryCatchAspect {

    @Around("execution(!synthetic * *(..)) && onSafe()")
    public Object doSafeMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        return safeMethod(joinPoint);
    }

    @Pointcut("@within(com.meiyou.jet.annotation.JTryCatch)||@annotation(com.meiyou.jet.annotation.JTryCatch)")
    public void onSafe() {
    }

    private Object safeMethod(final ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = null;
        try {
            result = joinPoint.proceed(joinPoint.getArgs());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }

//    private static String getStringFromException(Throwable ex) {
//        StringWriter errors = new StringWriter();
//        ex.printStackTrace(new PrintWriter(errors));
//        return errors.toString();
//    }
}