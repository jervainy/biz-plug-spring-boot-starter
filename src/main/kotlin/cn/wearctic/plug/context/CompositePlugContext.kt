package cn.wearctic.plug.context

import org.springframework.expression.EvaluationContext
import org.springframework.expression.spel.support.StandardEvaluationContext
import java.lang.reflect.Method

class CompositePlugContext: PlugContext {

    private val ctx: EvaluationContext = StandardEvaluationContext()

    override fun register(name: String, value: Any?) = ctx.setVariable(name, value)

    override fun register(params: Map<String, *>) = params.forEach { (t, u) -> register(t, u) }

    override fun register(name: String, method: Method) = (ctx as StandardEvaluationContext).registerFunction(name, method)

    @Suppress("UNCHECKED_CAST")
    override fun <E> getVariable(name: String, clz: Class<E>): E? = ctx.lookupVariable(name) as E?

    fun getEvaluationContext(): EvaluationContext = ctx

}