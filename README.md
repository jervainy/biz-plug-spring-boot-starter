<p align="center">
  <a href="https://search.maven.org/search?q=g:cn.wearctic%20a:biz-plug-spring-boot-starter">
    <img alt="maven" src="https://img.shields.io/maven-central/v/cn.wearctic/biz-plug-spring-boot-starter.svg?style=flat-square">
  </a>

  <a href="https://www.apache.org/licenses/LICENSE-2.0">
    <img alt="code style" src="https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square">
  </a>
</p>

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