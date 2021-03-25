package cn.wearctic.plug

import cn.wearctic.plug.annotations.PlugEntrance
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.expression.EvaluationContext

@Aspect
open class PlugEntranceAspect(private val mapInfo: MutableMap<String, MutableList<Invocation>>) {

    @AfterReturning(pointcut = "@annotation(cn.wearctic.plug.annotations.PlugEntrance)", returning = "retVal")
    open fun handle(joinPoint: JoinPoint, retVal: Any) {
        val signature = joinPoint.signature
        if (signature is MethodSignature) {
            if (retVal is Map<*, *>) {
                retVal.forEach { k, u -> k?.let { PlugInvocationContext.register(it.toString(), u) } }
            } else {
                PlugInvocationContext.register("retVal", retVal)
            }
            val annotation = AnnotationUtils.findAnnotation(signature.method, PlugEntrance::class.java)
            mapInfo.getOrDefault(annotation.key, emptyList()).forEach {
                it.method.parameterTypes
                it.method.invoke(it.target)
            }
        }
    }

}