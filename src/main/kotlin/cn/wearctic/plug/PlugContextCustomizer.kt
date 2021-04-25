package cn.wearctic.plug;

import cn.wearctic.plug.context.PlugContext;

@FunctionalInterface
interface PlugContextCustomizer {

    fun customizer(context: PlugContext)

}
