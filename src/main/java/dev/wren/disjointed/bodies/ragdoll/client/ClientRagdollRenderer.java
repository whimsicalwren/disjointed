package dev.wren.disjointed.bodies.ragdoll.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.wren.disjointed.bodies.ragdoll.RagdollSlots;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import org.valkyrienskies.core.api.bodies.ClientVsBody;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@FunctionalInterface
public interface ClientRagdollRenderer<E extends Enum<E>> {

    void render(ClientRagdoll<E> ragdoll, ClientLevel level, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, Vec3 camPos);

    default ClientVsBody getBody(ClientLevel level, Long id) {
        return VSGameUtilsKt.getShipObjectWorld(level).getAllBodies().getById(id);
    }
}