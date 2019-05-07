# TransmittableThreadLocal 

## 简介

​	**TransmittableThreadLocal** 是Alibaba开源的、用于解决 **“在使用线程池等会缓存线程的组件情况下传递ThreadLocal”** 问题的 InheritableThreadLocal 扩展。若希望 TransmittableThreadLocal 在线程池与主线程间传递，需配合 *TtlRunnable* 和 *TtlCallable* 使用。

## 使用场景

下面是几个典型场景例子。

* 分布式跟踪系统

* 应用容器或上层框架跨应用代码给下层SDK传递信息

* 日志收集记录系统上下文



