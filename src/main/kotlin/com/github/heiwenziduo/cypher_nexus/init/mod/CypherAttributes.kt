package com.github.heiwenziduo.cypher_nexus.init.mod

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.attribute.CypherAttribute
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.attribute.CypherAttribute.AttributeTarget
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.RegistryBuilder
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

/**
 * registry attribute keys
 * */
object CypherAttributes {
    val RESOURCE_KEY: ResourceKey<Registry<CypherAttribute>> =
        ResourceKey.createRegistryKey(CypherNexus.modResource("cypher/attribute"))
    val REGISTRY: Registry<CypherAttribute> = RegistryBuilder(RESOURCE_KEY).create()

    val DEFERRED_REGISTER: DeferredRegister<CypherAttribute> =
        DeferredRegister.create(REGISTRY, CypherNexus.MOD_ID)

    fun register() {
        DEFERRED_REGISTER.register(MOD_BUS)
    }

    // TODO, maybe a chain is more well-received
    fun registerAttribute(path: String, default: Double, min: Double = -Double.MAX_VALUE, max: Double = Double.MAX_VALUE, target: AttributeTarget = AttributeTarget.PROJECTILE): Holder<CypherAttribute> =
        DEFERRED_REGISTER.register(path) { resource ->
            CypherAttribute(
                resource = resource,
                defaultValue = default,
                min = min, max = max,
                sync = true, target =  target)
        }


    // ================================ casting process
//    val MANA_DRAIN = registerAttribute("mana_drain")
//    val DRAW = registerAttribute("draw")
    /** unit is "tick", cast to int at last */
    val CAST_DELAY = registerAttribute("cast_delay", 0.0, target = AttributeTarget.CASTING)
    /** unit is "tick", cast to int at last */
    val RECHARGE_TIME = registerAttribute("recharge_time", 0.0, target = AttributeTarget.CASTING)
    /** degree */
    val SPREAD = registerAttribute("spread", 0.0, 0.0, 720.0, target = AttributeTarget.CASTING)
    val RECOIL = registerAttribute("recoil", 0.0, 0.0, 10.0, target = AttributeTarget.CASTING)


    // ================================ projectile
    val DAMAGE = registerAttribute("damage", 0.0)
    /** block per tick, but will show block/sec */
    val SPEED = registerAttribute("speed", 0.0, 0.0, 8.0)
    /** tick */
    val EXISTING = registerAttribute("existing", 20.0)
    val RADIUS = registerAttribute("redius", 1.0, 0.125, 16.0)
    /** int, bounce times */
    val BOUNCE = registerAttribute("bounce", 0.0, 0.0, 50.0)
    /** 1 -> 100% */
    val CRIT_CHANCE = registerAttribute("crit_chance", 0.0, 0.0)
}