package cn.wearctic.plug

import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Role
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.filter.GenericFilterBean
import java.util.concurrent.ConcurrentHashMap
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

@Configuration
open class PlugAutoConfiguration {

    private val plugMapInfo: ConcurrentHashMap<String, MutableList<Invocation>> = ConcurrentHashMap()

    @ConditionalOnMissingBean
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    open fun plugBeanPostProcessor() = PlugAnnotationBeanPostProcessor(plugMapInfo)

    @ConditionalOnMissingBean
    @Bean
    open fun plugEntranceAspect() = PlugEntranceAspect(plugMapInfo)

    @Order(Ordered.LOWEST_PRECEDENCE)
    @Bean
    open fun plugFilter() = object: GenericFilterBean() {
        override fun doFilter(p0: ServletRequest?, p1: ServletResponse?, p2: FilterChain) {
            p2.doFilter(p0, p1)
            PlugInvocationContext.clear()
        }
    }

}