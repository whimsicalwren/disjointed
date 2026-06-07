package dev.wren.disjointed.bodies.ragdoll.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;

@FunctionalInterface
public interface ClientRagdollRenderer {
    void render(ClientRagdoll ragdoll, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, Vec3 camPos);

    ClientRagdollRenderer NONE = (r, p, b, c) -> {};
}