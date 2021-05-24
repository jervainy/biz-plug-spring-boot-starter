<p align="center">
  <a href="https://search.maven.org/search?q=g:cn.wearctic%20a:biz-plug-spring-boot-starter">
    <img alt="maven" src="https://img.shields.io/maven-central/v/cn.wearctic/biz-plug-spring-boot-starter.svg?style=flat-square">
  </a>

  <a href="https://www.apache.org/licenses/LICENSE-2.0">
    <img alt="code style" src="https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square">
  </a>
</p>

A simple Business Plug-in

#### Usage
1. Define the entry point
```java
@PlugEntrance(key = "check")
public Map<String, Object> check() {
    ....
}
```
The method return value is written back to the PlugInvocationContext, variables can be obtained in subsequent use `PlugInvocationContext.getVariable("xxxx")`

2. Plug-in processing
```java
@PlugHandler(key = "check")
public void versionCheck() {
    PlugInvocationContext.getVariable("xxxxx");
    .....
}
```
PlugInvocationContext will be cleared after the request ends