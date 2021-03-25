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