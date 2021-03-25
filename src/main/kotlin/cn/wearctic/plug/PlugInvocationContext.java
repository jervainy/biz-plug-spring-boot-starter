package cn.wearctic.plug;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

public class PlugInvocationContext {

    private final static ThreadLocal<EvaluationContext> invocationCtx = new  ThreadLocal<>();

    private static EvaluationContext getOrDefault() {
        EvaluationContext ctx = invocationCtx.get();
        if (ctx == null) {
            ctx = new StandardEvaluationContext();
        }
        return ctx;
    }

    public static void register(String variableName, Object obj) {
        EvaluationContext ctx = getOrDefault();
        ctx.setVariable(variableName, obj);
        invocationCtx.set(ctx);
    }

    public static void register(Map<String, Object> params) {
        EvaluationContext ctx = getOrDefault();
        params.forEach(ctx::setVariable);
        invocationCtx.set(ctx);
    }

    public static Object getVariable(String variableName) {
        return getVariable(variableName, Object.class);
    }

    @SuppressWarnings("unchecked")
    public static <E> E getVariable(String variableName, Class<E> clz) {
        EvaluationContext ctx = invocationCtx.get();
        if (ctx == null) {
            return null;
        }
        return (E) ctx.lookupVariable(variableName);
    }

    static EvaluationContext getContext() {
        return invocationCtx.get();
    }

    static void clear() {
        invocationCtx.remove();
    }

}
