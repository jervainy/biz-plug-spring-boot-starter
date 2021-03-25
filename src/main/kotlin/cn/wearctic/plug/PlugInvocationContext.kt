package cn.wearctic.plug

import org.springframework.expression.EvaluationContext
import org.springframework.expression.spel.support.StandardEvaluationContext

object PlugInvocationContext {

    private val invocationCtx: ThreadLocal<EvaluationContext> = ThreadLocal()

    fun register(variableName: String, obj: Any?) {
        val ctx = invocationCtx.get()?: StandardEvaluationContext()
        ctx.setVariable(variableName, obj)
        invocationCtx.set(ctx)
    }

    fun register(params: Map<String, Any>) {
        val ctx = invocationCtx.get()?: StandardEvaluationContext()
        params.forEach { (t, u) -> ctx.setVariable(t, u) }
        invocationCtx.set(ctx)
    }

    fun getVariable(variableName: String): Any? {
        val ctx = invocationCtx.get()?: return null
        return ctx.lookupVariable(variableName)
    }

    internal fun clear() = invocationCtx.remove()


}