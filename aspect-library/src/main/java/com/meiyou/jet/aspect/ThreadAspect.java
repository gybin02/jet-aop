package com.meiyou.jet.aspect;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 异步线程执行，切面,借助rxjava,异步执行app中的方法
 */
@Aspect
public class ThreadAspect {
    private static final String TAG = "ThreadAspect";

    @Around("execution(!synthetic * *(..)) && onAsyncMethod()")
    public void doAsyncMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        asyncMethod(joinPoint);
    }

    @Pointcut("@within(com.meiyou.jet.annotation.JThread)||@annotation(com.meiyou.jet.annotation.JThread)")
    public void onAsyncMethod() {
    }

    private void asyncMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
    
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.w(TAG, "线程里运行");
                    Object result = joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }).start();

// 
//        Observable.create(new Observable.OnSubscribe<Object>() {
//
//            @Override
//            public void call(Subscriber<? super Object> subscriber) {
//                Looper.prepare();
//                try {
//                    joinPoint.proceed();
//                } catch (Throwable throwable) {
//                    throwable.printStackTrace();
//                }
//                Looper.loop();
//            }
//        })
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe();
    }
}