package dev.wren.disjointed.bodies.ragdoll.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.wren.disjointed.Disjointed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientRagdollManager {

    private static final Map<UUID, ClientRagdoll> CLIENT_RAGDOLLS = new HashMap<>();

    public static void register(ClientRagdoll ragdoll) {
        System.out.println("registered " + ragdoll.uuid());
        CLIENT_RAGDOLLS.put(ragdoll.uuid(), ragdoll);
    }

    public static void unregister(UUID uuid) {
        System.out.println("unregistered " + uuid);
        CLIENT_RAGDOLLS.remove(uuid);
    }

    public static void clearAll() {
        CLIENT_RAGDOLLS.clear();
    }

    @Mod.EventBusSubscriber(modid = Disjointed.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class Renderer {

        @SubscribeEvent
        public static void onRenderLevel(RenderLevelStageEvent event) {
            if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;
            if (CLIENT_RAGDOLLS.isEmpty()) return;

            System.out.println("a");

            PoseStack poseStack = event.getPoseStack();
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            Vec3 camPos = event.getCamera().getPosition();

            for (ClientRagdoll ragdoll : CLIENT_RAGDOLLS.values()) {
                System.out.println("rendering " + ragdoll.uuid());
                ClientRagdollGroupRegistry.get(ragdoll.typeId()).render(ragdoll, poseStack, bufferSource, camPos);
            }

            bufferSource.endBatch();
        }
    }



}
