package dev.wren.disjointed.events;

import dev.wren.disjointed.Disjointed;
import dev.wren.disjointed.bodies.ragdoll.client.ClientRagdollManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Disjointed.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        ClientRagdollManager.onRenderLevel(event);
    }

}
