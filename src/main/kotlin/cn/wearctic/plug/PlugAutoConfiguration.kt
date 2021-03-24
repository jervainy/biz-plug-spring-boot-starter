package cn.wearctic.plug

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PlugAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    fun plugBeanPostProcessor() = PlugAnnotationBeanPostProcessor()

    @ConditionalOnMissingBean
    @Bean
    fun plugEntranceAspect() = PlugEntranceAspect()

}