### AspectJ 使用例子
在编译期对目标对象、方法做标记，对目标类、方法进行重构，将PointCut插入目标中，截获该目标的信息以及上下文环境，以达到非侵入代码监控的目的——注意，它只能获得对象的声明，如果对象的声明式接口，那么默认情况下（不使用this、target约束切点），获取的是声明类型，而不是具体运行时的类。

1、编写Aspect：声明Aspect、PointCut和Advise。

2、ajc编织： AspectJ编译器在编译期间对所切点所在的目标类进行了重构，在编译层将AspectJ程序与目标程序进行双向关联，生成新的目标字节码，即将AspectJ的切点和其余辅助的信息类段插入目标方法和目标类中，同时也传回了目标类以及其实例引用。这样便能够在AspectJ程序里对目标程序进行监听甚至操控。

3、execution： 顾名思义，它截获的是方法真正执行的代码区，Around方法块就是专门为它存在的。调用Around可以控制原方法的执行与否，可以选择执行也可以选择替换。


```java
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Path;
import android.os.Build;

import org.android10.gintonic.internal.ChooseDialog;
import org.android10.gintonic.internal.DebugLog;
import org.android10.gintonic.internal.MethodMsg;
import org.android10.gintonic.internal.StopWatch;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 截获类名最后含有Activity、Layout的类的所有方法
 * 监听目标方法的执行时间
 */
@Aspect
public class TraceAspect {
  private static Object currentObject = null;
  //进行类似于正则表达式的匹配，被匹配到的方法都会被截获
  ////截获任何包中以类名以Activity、Layout结尾，并且该目标类和当前类是一个Object的对象的所有方法
  private static final String POINTCUT_METHOD =
      "(execution(* *..Activity+.*(..)) ||execution(* *..Layout+.*(..))) && target(Object) && this(Object)";
   //精确截获MyFrameLayou的onMeasure方法
    private static final String POINTCUT_CALL = "call(* org.android10.viewgroupperformance.component.MyFrameLayout.onMeasure(..))";

  private static final String POINTCUT_METHOD_MAINACTIVITY = "execution(* *..MainActivity+.onCreate(..))";

  //切点，ajc会将切点对应的Advise编织入目标程序当中
  @Pointcut(POINTCUT_METHOD)
  public void methodAnnotated() {}
  @Pointcut(POINTCUT_METHOD_MAINACTIVITY)
  public void methodAnootatedWith(){}

    /**
     * 在截获的目标方法调用之前执行该Advise
     * @param joinPoint
     * @throws Throwable
     */
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  @Before("methodAnootatedWith()")
  public void onCreateBefore(JoinPoint joinPoint) throws Throwable{
      Activity activity = null;
      //获取目标对象
      activity = ((Activity)joinPoint.getTarget());
      //插入自己的实现，控制目标对象的执行
      ChooseDialog dialog = new ChooseDialog(activity);
      dialog.show();

      //做其他的操作
      buildLogMessage("test",20);
  }
    /**
     * 在截获的目标方法调用返回之后（无论正常还是异常）执行该Advise
     * @param joinPoint
     * @throws Throwable
     */
 @After("methodAnootatedWith()")
  public void onCreateAfter(JoinPoint joinPoint) throws Throwable{
      Log.e("onCreateAfter:","onCreate is end .");

  }
    /**
     * 在截获的目标方法体开始执行时（刚进入该方法实体时）调用
     * @param joinPoint
     * @return
     * @throws Throwable
     */
  @Around("methodAnnotated()")
  public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {

    if (currentObject == null){
        currentObject = joinPoint.getTarget();
    }
      //初始化计时器
    final StopWatch stopWatch = new StopWatch();
      //开始监听
      stopWatch.start();
      //调用原方法的执行。
    Object result = joinPoint.proceed();
      //监听结束
    stopWatch.stop();
      //获取方法信息对象
      MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
      String className;
      //获取当前对象，通过反射获取类别详细信息
      className = joinPoint.getThis().getClass().getName();

      String methodName = methodSignature.getName();
    if (currentObject != null && currentObject.equals(joinPoint.getTarget())){
        DebugLog.log(new MethodMsg(className, buildLogMessage(methodName, stopWatch.getTotalTimeMicros()),stopWatch.getTotalTimeMicros()));
    }else if(currentObject != null && !currentObject.equals(joinPoint.getTarget())){
        DebugLog.log(new MethodMsg(className, buildLogMessage(methodName, stopWatch.getTotalTimeMicros()),stopWatch.getTotalTimeMicros()));
        currentObject = joinPoint.getTarget();
        DebugLog.outPut(new Path());    //日志存储
        DebugLog.ReadIn(new Path());    //日志读取
    }
    return result;
  }

  /**
   * 创建一个日志信息
   *
   * @param methodName 方法名
   * @param methodDuration 执行时间
   * @return
   */
  private static String buildLogMessage(String methodName, long methodDuration) {
    StringBuilder message = new StringBuilder();
    message.append(methodName);
    message.append(" --> ");
    message.append("[");
    message.append(methodDuration);
    if (StopWatch.Accuracy == 1){
        message.append("ms");
    }else {
        message.append("mic");
    }
    message.append("]      ");

    return message.toString();
  }

}
```