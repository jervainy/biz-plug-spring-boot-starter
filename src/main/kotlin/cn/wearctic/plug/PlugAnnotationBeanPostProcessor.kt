package cn.wearctic.plug

import cn.wearctic.plug.annotations.PlugHandler
import org.springframework.aop.support.AopUtils
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.core.Ordered
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.util.ReflectionUtils

open class PlugAnnotationBeanPostProcessor: BeanPostProcessor, Ordered {

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        val targetClass = AopUtils.getTargetClass(bean)
        val mapInfo: MutableMap<String, MutableList<Invocation>> = mutableMapOf()
        ReflectionUtils.doWithMethods(targetClass) { method ->
            val annotation = AnnotationUtils.findAnnotation(method, PlugHandler::class.java)
            annotation?.let {
                val key = it.key
                val list = mapInfo.getOrDefault(key, mutableListOf())
                list.add(Invocation(method, bean, order = it.order, condition = it.condition))
                mapInfo[key] = list
            }
        }
        mapInfo.forEach { (t, u) -> PlugInvocationContext.addPlugMapInfo(t, u) }
        return super.postProcessAfterInitialization(bean, beanName)
    }

    override fun getOrder(): Int = Ordered.LOWEST_PRECEDENCE

}