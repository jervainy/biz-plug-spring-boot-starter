package cn.wearctic.plug

import cn.wearctic.plug.annotations.PlugEntrance
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.core.annotation.AnnotationUtils

@Aspect
open class PlugEntranceAspect {

    @AfterReturning(pointcut = "@annotation(cn.wearctic.plug.annotations.PlugEntrance)", returning = "retVal")
    open fun handle(joinPoint: JoinPoint, retVal: Any) {
        val signature = joinPoint.signature
        if (signature is MethodSignature) {
            val annotation = AnnotationUtils.findAnnotation(signature.method, PlugEntrance::class.java)
            if (retVal is Map<*, *>) {
                PlugInvocationContext.invokePlug(annotation.key, retVal as MutableMap<String, *>?)
            } else {
                PlugInvocationContext.register("retVal", retVal)
                PlugInvocationContext.invokePlug(annotation.key)
            }
        }
    }

}