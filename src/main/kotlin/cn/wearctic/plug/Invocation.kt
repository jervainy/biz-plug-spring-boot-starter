package cn.wearctic.plug

import java.lang.reflect.Method

data class Invocation(val method: Method, val target: Any, val order: Int = 0, val condition: String)