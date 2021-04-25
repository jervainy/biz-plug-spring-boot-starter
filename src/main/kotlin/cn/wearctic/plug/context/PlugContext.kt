package cn.wearctic.plug.context

import java.lang.reflect.Method

interface PlugContext {

    fun register(name: String, value: Any?)

    fun register(params: Map<String, *>)

    fun register(name: String, method: Method)

    fun <E> getVariable(name: String, clz: Class<E>): E?

}