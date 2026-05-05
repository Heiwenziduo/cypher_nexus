package com.github.heiwenziduo.cypher_nexus.client.network

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.network.CypherEntitySyncData
import net.neoforged.neoforge.network.handling.IPayloadContext

/*
 * define payload TYPE by implementing CustomPacketPayload ->
 * register payload through RegisterPayloadHandlersEvent ->
 * register payload handler on desired side (client here) ->
 * use PacketDistributor.sendToPlayer method to send package
 * */
object ClientPayloadHandler {
    fun cypherEntitySyncData(data: CypherEntitySyncData, context: IPayloadContext) {
        // Do something with the data, on the network thread, heavy computation should be done before pass to main thread
        println("client receive package -> cypherEntitySyncData: \n$data")

        // Do something with the data, on the main thread
        context.enqueueWork() {

        }.exceptionally {
            // Handle exception
            // context.disconnect(Component.translatable("my_mod.networking.failed", it.message)) // this kicks player out of the logical server
            CypherNexus.LOGGER.warn(it.message)
            return@exceptionally null
        }
    }
}