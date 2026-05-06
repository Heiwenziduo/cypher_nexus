package com.github.heiwenziduo.cypher_nexus.mechanic.cypher.hook

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.AbstractCypher
import org.apache.logging.log4j.util.BiConsumer
import java.util.function.Supplier

class HookContainer {
    // TODO consider sync hook-map to client
    private val map = HashMap<HookModule<*>, HashMap<AbstractCypher, Int>>()

    fun add(module: HookModule<*>, cypher: AbstractCypher) {
        if (module.hook.isInstance(cypher)) {

            val cypherMap = map.getOrPut(module) { HashMap() }
            cypherMap[cypher] = cypherMap.getOrDefault(cypher, 0) + 1

        } else {
            // a cypher registered a HookModule it doesn't actually implement.
            CypherNexus.LOGGER.error("Cypher $cypher claimed to have module ${module.resource} but doesn't implement it!")
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(module: HookModule<T>): Map<T, Int> {
        val innerMap = map[module] ?: return emptyMap()

        // because we strictly checked `isInstance` inside the add() method,
        // we mathematically guarantee that every AbstractCypher inside this specific
        // innerMap implements the interface 'T'. Therefore, we can cast the whole map safely
        return innerMap as Map<T, Int>
    }
    fun <T : Any> get(module: Supplier<out HookModule<T>>) = get(module.get())


    inline fun <T : Any> playHooks(module: HookModule<T>, action: (T, Int) -> Unit) {
        for ((hook, level) in get(module)) {
            action(hook, level)
        }
    }
    inline fun <T : Any> playHooks(module: Supplier<out HookModule<T>>, action: (T, Int) -> Unit) {
        playHooks(module.get(), action)
    }
}