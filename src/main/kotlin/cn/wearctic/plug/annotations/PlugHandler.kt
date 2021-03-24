package cn.wearctic.plug.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention
@MustBeDocumented
annotation class PlugHandler(val key: String, val order: Int = 0)