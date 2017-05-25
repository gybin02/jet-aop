## Jet AOP 
开发该项目的原因是基于还没有发现目前的开源库中比较好的AOP框架或者工具，虽然xposed，dexposed非常强大，但由于Android的碎片化比较严重，兼容问题永远是一座无法逾越的大山.

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
    compile 'com.safframework:saf-aop:1.1.0'
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


### License
-------
Copyright 2017 zhengxiaobin@xiaoyouzi.com
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.