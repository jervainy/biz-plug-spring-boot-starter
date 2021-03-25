package cn.wearctic

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy

interface Proxy0 {

    fun sayHello(): String

}

fun main() {

    val target = object : Proxy0 {
        override fun sayHello(): String {
            return "sayHello"
        }
    }

    val invocationHandler = InvocationHandler { proxy, method, args ->
        return@InvocationHandler method.invoke(target, args)
    }

    val obj: Proxy0 =
        Proxy.newProxyInstance(invocationHandler.javaClass.classLoader, target.javaClass.interfaces, invocationHandler) as Proxy0
    println(obj.javaClass)

}