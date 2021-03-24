package cn.wearctic.plug.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention
@MustBeDocumented
annotation class PlugEntrance(val key: String)