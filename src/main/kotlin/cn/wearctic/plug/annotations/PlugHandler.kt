package cn.wearctic.plug.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class PlugHandler(val key: String, val order: Int = 0, val condition: String = "")