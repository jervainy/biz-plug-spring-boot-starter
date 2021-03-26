package cn.wearctic.plug;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PlugInvocationContext {

    private final static Logger log = LoggerFactory.getLogger(PlugInvocationContext.class);

    private final static ConcurrentHashMap<String, List<Invocation>> plugMapInfo = new ConcurrentHashMap<>();
    private final static ThreadLocal<EvaluationContext> invocationCtx = new  ThreadLocal<>();
    private final static ExpressionParser parser = new SpelExpressionParser();

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

    public static void register(Map<String, ?> params) {
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

    public static boolean evaluateBool(String expression) {
        return evaluateExpression(expression, Boolean.class);
    }

    public static <E> E evaluateExpression(String expression, Class<E> clz) {
        Expression exp = parser.parseExpression(expression);
        return exp.getValue(getContext(), clz);
    }

    public static void invokePlug(String key) {
        invokePlug(key, null);
    }

    public static void invokePlug(String key, Map<String, ?> variables) {
        if (variables != null) {
            register(variables);
        }
        plugMapInfo.getOrDefault(key, Collections.emptyList()).forEach(it -> {
            if (it.getCondition().isEmpty() || evaluateBool(it.getCondition())) {
                try {
                    it.getMethod().invoke(it.getTarget());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.error(e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        });
    }


    static EvaluationContext getContext() {
        return invocationCtx.get();
    }

    static void addPlugMapInfo(String key, List<Invocation> list) {
        List<Invocation> objList = plugMapInfo.getOrDefault(key, new ArrayList<>());
        objList.addAll(list);
        objList.sort(Comparator.comparingInt(Invocation::getOrder));
        plugMapInfo.put(key, objList);
    }

    static void clear() {
        invocationCtx.remove();
    }

}
