package cn.wearctic.plug

import org.springframework.beans.factory.config.BeanPostProcessor

class PlugAnnotationBeanPostProcessor: BeanPostProcessor {

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        return super.postProcessAfterInitialization(bean, beanName)
    }

}