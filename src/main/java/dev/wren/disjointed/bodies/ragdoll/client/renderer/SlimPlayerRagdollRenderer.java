package dev.wren.disjointed.bodies.ragdoll.client.renderer;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;

public class SlimPlayerRagdollRenderer extends PlayerRagdollRenderer {
    @Override
    public ModelLayerLocation getModelLayer() {
        return ModelLayers.PLAYER_SLIM;
    }
}
