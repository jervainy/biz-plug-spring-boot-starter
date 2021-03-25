简单的业务插件

#### Usage
1. 定义切入点
```java
@PlugEntrance(key = "check")
public Map<String, Object> check() {
    ....
}
```
方法的返回值将会被写入到`PlugInvocationContext`中，后续使用可以获取`PlugInvocationContext.getVariable("xxxx")`

2. 插件处理
```java
@PlugHandler(key = "check")
public void versionCheck() {
    PlugInvocationContext.getVariable("xxxxx");
    .....
}
```
PlugInvocationContext将会在请求结束后清除