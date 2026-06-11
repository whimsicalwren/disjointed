package dev.wren.disjointed.bodies.ragdoll.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public interface ModelPartExtension {
    void renderWithoutChildren(PoseStack p_104307_, VertexConsumer p_104308_, int p_104309_, int p_104310_, float p_104311_, float p_104312_, float p_104313_, float p_104314_);

    default void renderWithoutChildren(PoseStack p_104302_, VertexConsumer p_104303_, int p_104304_, int p_104305_) {
        renderWithoutChildren(p_104302_, p_104303_, p_104304_, p_104305_, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
