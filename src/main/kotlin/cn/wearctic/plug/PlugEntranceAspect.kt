package cn.wearctic.plug

import cn.wearctic.plug.annotations.PlugEntrance
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.expression.spel.standard.SpelExpressionParser

@Aspect
open class PlugEntranceAspect(private val mapInfo: MutableMap<String, MutableList<Invocation>>) {

    private val parser = SpelExpressionParser()

    @AfterReturning(pointcut = "@annotation(cn.wearctic.plug.annotations.PlugEntrance)", returning = "retVal")
    open fun handle(joinPoint: JoinPoint, retVal: Any) {
        val signature = joinPoint.signature
        if (signature is MethodSignature) {
            if (retVal is Map<*, *>) {
                retVal.forEach { k, u -> k?.let { PlugInvocationContext.register(it.toString(), u) } }
            } else {
                PlugInvocationContext.register("retVal", retVal)
            }
            val ctx = PlugInvocationContext.getContext()
            val annotation = AnnotationUtils.findAnnotation(signature.method, PlugEntrance::class.java)
            mapInfo.getOrDefault(annotation.key, emptyList()).forEach {
                if (it.condition.isBlank() || parser.parseExpression(it.condition).getValue(ctx, Boolean::class.java)) {
                    it.method.invoke(it.target)
                }
            }
        }
    }

}