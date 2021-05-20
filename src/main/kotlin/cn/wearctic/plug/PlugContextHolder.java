package cn.wearctic.plug;

import cn.wearctic.plug.context.CompositePlugContext;
import cn.wearctic.plug.context.PlugContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PlugContextHolder {

    private final static Logger log = LoggerFactory.getLogger(PlugContextHolder.class);

    private final static ConcurrentHashMap<String, List<Invocation>> plugMapInfo = new ConcurrentHashMap<>();
    private final static ThreadLocal<PlugContext> invocationCtx = new  ThreadLocal<>();
    private final static ExpressionParser parser = new SpelExpressionParser();
    private final static List<PlugContextCustomizer> customizers = new ArrayList<>();

    private static PlugContext getOrDefault() {
        PlugContext ctx = invocationCtx.get();
        if (ctx == null) {
            CompositePlugContext obj = new CompositePlugContext();
            ctx = obj;
            invocationCtx.set(ctx);
            customizers.forEach(it -> it.customizer(obj));
        }
        return ctx;
    }

    public static void register(String variableName, Object obj) {
        PlugContext ctx = getOrDefault();
        ctx.register(variableName, obj);
    }

    public static void register(Map<String, ?> params) {
        PlugContext ctx = getOrDefault();
        ctx.register(params);
    }

    public static void register(String name, Method method) {
        PlugContext ctx = getOrDefault();
        ctx.register(name, method);
    }

    public static Object getVariable(String variableName) {
        return getVariable(variableName, Object.class);
    }

    public static <E> E getVariable(String variableName, Class<E> clz) {
        PlugContext ctx = getOrDefault();
        return ctx.getVariable(variableName, clz);
    }

    public static boolean evaluateBool(String expression) {
        return evaluateExpression(expression, Boolean.class);
    }

    public static <E> E evaluateExpression(String expression, Class<E> clz) {
        CompositePlugContext ctx = (CompositePlugContext) getOrDefault();
        Expression exp = parser.parseExpression(expression);
        return exp.getValue(ctx.getEvaluationContext(), clz);
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
                } catch (InvocationTargetException e) {
                    log.error(e.getMessage());
                    if (e.getTargetException() instanceof RuntimeException) {
                        throw (RuntimeException) e.getTargetException();
                    }
                    throw new RuntimeException(e.getTargetException());
                } catch (IllegalAccessException e) {
                    log.error(e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        });
    }

    static void addPlugMapInfo(String key, List<Invocation> list) {
        List<Invocation> objList = plugMapInfo.getOrDefault(key, new ArrayList<>());
        objList.addAll(list);
        objList.sort(Comparator.comparingInt(Invocation::getOrder));
        plugMapInfo.put(key, objList);
    }

    static void addCustomizer(PlugContextCustomizer customizer) {
        customizers.add(customizer);
    }

    static void clear() {
        invocationCtx.remove();
    }

}
