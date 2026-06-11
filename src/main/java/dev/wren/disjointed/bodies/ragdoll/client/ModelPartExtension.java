package dev.wren.disjointed.bodies.ragdoll.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public interface ModelPartExtension {
    void renderWithoutChildren(PoseStack ms, VertexConsumer consumer, int light, int overlay, float r, float g, float b, float a);

    default void renderWithoutChildren(PoseStack ms, VertexConsumer consumer, int light, int overlay) {
        renderWithoutChildren(ms, consumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
