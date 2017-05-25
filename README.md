## Jet AOP 
美柚AOP框架；虽然xposed，dexposed非常强大，但由于Android的碎片化比较严重，兼容问题永远是一座无法逾越的大山. 因此考虑用AspectJ来实现；

### Fetures

### Download
在根目录下的build.gradle中添加
```groovy
buildscript {
     repositories {
         jcenter()
     }
     dependencies {
         classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:1.0.8'
     }
 }
```
在app 模块目录下的build.gradle中添加

```groovy
apply plugin: 'com.hujiang.android-aspectjx'

...

dependencies {
    compile 'com.meiyou.framework:jet-aop:0.0.1-SNAPSHOT'
    ...
}
```
基于aspectj的AOP，无需使用耗费性能的反射.不过,需要在build.gradle中配置一下aspectj


### Use

| 注解名称        | 作用          | 备注          |
| ------------- |:-------------:| :-------------:|
| @JThread        |借助rxjava,异步执行app中的方法|       |
| @JLogMethod    |将方法的入参和出参都打印出来,可以用于调试|       |
| @JTryCatch        |可以安全地执行方法,而无需考虑是否会抛出运行时异常|       |
| @JLogTime        |用于追踪某个方法花费的时间,可以用于性能调优的评判|       |


@JThread的使用方法:
--------------
```Java
	@JThread
	private void useAsync() {
		Log.e(TAG, " thread=" + Thread.currentThread().getId());
		Log.e(TAG, "ui thread=" + Looper.getMainLooper().getThread().getId());
	}
```
@JLogMethod  等； 
--------------
    @JTryCatch
    @JLogTime
    @JLogMethod
    public int testLog(int k) {
        int i = k + 100;
        int j = i++;
        j=j/0;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return j;
    }

LogCat 打印：
```
 MainActivity.testLog(..) 方法参数 : [10]
 MainActivity.testLog(..) 方法 返回值 : 0
 MainActivity.testLog 执行时间： [3ms]
```
### Problem
* Android Studio的Instant Run功能有时会对你的编译有影响，当出现这种情况时，关闭Instant Run功能，
* aspectj代码编译时会报一些如下的错，找到对应的库，把这个库过滤掉就可以了。

### aspectjx配置

aspectjx默认会遍历项目编译后所有的.class文件和依赖的第三方库去查找符合织入条件的切点，为了提升编译效率，可以加入过滤条件指定遍历某些库或者不遍历某些库。

`includeJarFilter`和`excludeJarFilter`可以支持`groupId`过滤，`artifactId`过滤，或者依赖路径匹配过滤

```
aspectjx {
	//织入遍历符合条件的库
	includeJarFilter 'universal-image-loader', 'AspectJX-Demo/library'
	//排除包含‘universal-image-loader’的库
	excludeJarFilter 'universal-image-loader'
}
```

. 忽略groupId为`org.apache.httpcomponents`的库

```
aspectjx {
	excludeJarFilter 'org.apache.httpcomponents'
}
```
. 忽略artifactId为`gson`的库

```
	aspectjx {
		excludeJarFilter 'gson'
	}
```

. 忽略jar `alisdk-tlog-1.jar`

```
	aspectjx {
		excludeJarFilter 'alisdk-tlog-1'
	}
```


* 忽略所有依赖的库

```
aspectjx {
	excludeJarFilter '.jar'
}
```

### 参考

* [用aspectjx实现的简单、方便、省事的Android M动态权限配置框架](https://github.com/firefly1126/android_permission_aspectjx)

* [AspectJ官网](https://eclipse.org/aspectj/)

* [AspectJ Programming Guide](https://eclipse.org/aspectj/doc/released/progguide/index.html)

* [AspectJ Development Environment Guide](https://eclipse.org/aspectj/doc/released/devguide/index.html)

* [AspectJ NoteBook](https://eclipse.org/aspectj/doc/released/adk15notebook/index.html)

### License
-------
Copyright 2017 zhengxiaobin@xiaoyouzi.com
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.