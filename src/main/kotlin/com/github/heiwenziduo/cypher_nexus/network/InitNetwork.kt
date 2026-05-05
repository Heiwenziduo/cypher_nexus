package com.github.heiwenziduo.cypher_nexus.network

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.client.network.ClientPayloadHandler
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.registration.PayloadRegistrar


@EventBusSubscriber(modid = CypherNexus.MOD_ID)
object InitNetwork {
    @SubscribeEvent
    private fun registerPayload(event: RegisterPayloadHandlersEvent) {
        /* @doc
         * If you need to do some computation that is resource intensive,
         * then the work should be done on the network thread, instead of blocking the main thread.
         * */
        val registrar: PayloadRegistrar = event.registrar("114514")
//            .executesOn(HandlerThread.NETWORK)


        // remain unused for now...
        registrar.playToClient(
            CypherEntitySyncData.TYPE,
            CypherEntitySyncData.STREAM,
            ClientPayloadHandler::cypherEntitySyncData
        )
    }
}