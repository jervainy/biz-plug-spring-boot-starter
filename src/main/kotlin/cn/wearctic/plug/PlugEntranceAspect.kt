package cn.wearctic.plug

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning

class PlugEntranceAspect {

    @AfterReturning(pointcut = "@annotation(cn.wearctic.plug.annotations.PlugEntrance)", returning = "retVal")
    fun handle(joinPoint: JoinPoint, retVal: Any) {

    }

}