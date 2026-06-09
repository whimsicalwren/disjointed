package dev.wren.disjointed.bodies.ragdoll.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.wren.disjointed.bodies.ragdoll.client.ClientRagdoll;
import dev.wren.disjointed.bodies.ragdoll.client.ClientRagdollRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.valkyrienskies.core.api.bodies.ClientVsBody;
import org.valkyrienskies.core.api.bodies.properties.BodyTransform;

import java.util.Map;
import java.util.UUID;

public abstract class BaseRagdollRenderer<M extends EntityModel<?>> implements ClientRagdollRenderer {

    public void render(ClientRagdoll ragdoll, ClientLevel level, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, Vec3 camPos) {
        M model = getOrBakeModel(ragdoll.uuid());
        ResourceLocation texture = getOrLoadTexture(ragdoll);

        for (Map.Entry<String, Long> entry : ragdoll.pieces().entrySet()) {
            String slot = entry.getKey();
            ClientVsBody body = getBody(level, entry.getValue());
            if (body == null) continue;

            BodyTransform rt = body.getRenderTransform();
            Vector3dc pos = rt.getPosition();

            poseStack.pushPose();

            poseStack.translate(pos.x() - camPos.x, pos.y() - camPos.y, pos.z() - camPos.z);

            poseStack.mulPose(rt.getRotation().get(new Quaternionf()));
            Vector3d offset = getOffsetVector(slot);
            poseStack.translate(offset.x, offset.y, offset.z);

            poseStack.scale(-1f, -1f, 1f);

            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(texture));

            ModelPart part = getModelPartForSlot(slot, model);
            if (part != null) {
                part.render(poseStack, consumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
            }

            ModelPart layerPart = getModelLayerPartForSlot(slot, model);
            if (layerPart != null) {
                layerPart.render(poseStack, consumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
            }

            poseStack.popPose();
        }
    }

    protected abstract M getOrBakeModel(UUID uuid);

    protected abstract ResourceLocation getOrLoadTexture(ClientRagdoll ragdoll);

    protected abstract ModelPart getModelPartForSlot(String slot, M modelRoot);

    protected ModelPart getModelLayerPartForSlot(String slot, M modelRoot) {
        return null;
    }

    protected abstract Vector3d getOffsetVector(String slot);
}
