package cn.wearctic.plug

import cn.wearctic.plug.annotations.PlugHandler
import org.slf4j.LoggerFactory
import org.springframework.aop.support.AopUtils
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.core.Ordered
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.util.ReflectionUtils

open class PlugAnnotationBeanPostProcessor(private val mapInfo: MutableMap<String, MutableList<Invocation>>): BeanPostProcessor, Ordered {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        val targetClass = AopUtils.getTargetClass(bean)
        ReflectionUtils.doWithMethods(targetClass) { method ->
            val annotation = AnnotationUtils.findAnnotation(method, PlugHandler::class.java)
            annotation?.let {
                val key = it.key
                val list = mapInfo.getOrDefault(key, mutableListOf())
                list.add(Invocation(method, bean, it.order))
                list.sortBy { it0 -> it0.order }
                mapInfo[key] = list
            }
        }
        return super.postProcessAfterInitialization(bean, beanName)
    }

    override fun getOrder(): Int = Ordered.LOWEST_PRECEDENCE

}