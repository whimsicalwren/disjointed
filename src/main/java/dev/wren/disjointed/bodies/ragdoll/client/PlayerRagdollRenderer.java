package dev.wren.disjointed.bodies.ragdoll.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.wren.disjointed.bodies.ragdoll.RagdollSlots;
import dev.wren.disjointed.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3dc;
import org.valkyrienskies.core.api.bodies.ClientVsBody;
import org.valkyrienskies.core.api.bodies.properties.BodyTransform;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerRagdollRenderer implements ClientRagdollRenderer {

    private final Map<UUID, PlayerModel<AbstractClientPlayer>> models = new HashMap<>();
    private final Map<UUID, ResourceLocation> skins = new HashMap<>();

    @Override
    public void render(ClientRagdoll ragdoll, ClientLevel level, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, Vec3 camPos) {
        PlayerModel<AbstractClientPlayer> model = getOrBakeModel(ragdoll.uuid());
        ResourceLocation skin = getOrLoadSkin(ragdoll);

        for (Map.Entry<String, Long> entry : ragdoll.slots().entrySet()) {
            String slot = entry.getKey();
            ClientVsBody body = getBody(level, entry.getValue());
            if (body == null) continue;

            BodyTransform rt = body.getRenderTransform();
            Vector3dc pos = rt.getPosition();

            poseStack.pushPose();

            poseStack.translate(pos.x() - camPos.x, pos.y() - camPos.y, pos.z() - camPos.z);
            poseStack.mulPose(rt.getRotation().get(new Quaternionf()));
            poseStack.scale(-1f, -1f, 1f);

            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(skin));

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

    private PlayerModel<AbstractClientPlayer> getOrBakeModel(UUID uuid) {
        return models.computeIfAbsent(uuid, id -> {
            ModelPart root = Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER);
            return new PlayerModel<>(root, false);
        });
    }

    private ResourceLocation getOrLoadSkin(ClientRagdoll ragdoll) {
        if (skins.containsKey(ragdoll.uuid()))
            return skins.get(ragdoll.uuid());

        skins.put(ragdoll.uuid(), DefaultPlayerSkin.getDefaultSkin());

        String username = ragdoll.extraData().getString("username");
        if (username != null) {
            Utils.getSkin(username, skin -> skins.put(ragdoll.uuid(), skin));
        }

        return skins.get(ragdoll.uuid());
    }

    public void invalidate(UUID uuid) {
        models.remove(uuid);
        skins.remove(uuid);
    }

    private ModelPart getModelPartForSlot(String slot, PlayerModel<AbstractClientPlayer> modelRoot) {
        return switch (slot) {
            case RagdollSlots.Player.HEAD -> modelRoot.head;
            case RagdollSlots.Player.TORSO -> modelRoot.body;
            case RagdollSlots.Player.LEFT_ARM -> modelRoot.leftArm;
            case RagdollSlots.Player.RIGHT_ARM -> modelRoot.rightArm;
            case RagdollSlots.Player.LEFT_LEG -> modelRoot.leftLeg;
            case RagdollSlots.Player.RIGHT_LEG -> modelRoot.rightLeg;
            default -> null;
        };
    }

    private ModelPart getModelLayerPartForSlot(String slot, PlayerModel<AbstractClientPlayer> modelRoot) {
        return switch (slot) {
            case RagdollSlots.Player.HEAD -> modelRoot.hat;
            case RagdollSlots.Player.TORSO -> modelRoot.jacket;
            case RagdollSlots.Player.LEFT_ARM -> modelRoot.leftSleeve;
            case RagdollSlots.Player.RIGHT_ARM -> modelRoot.rightSleeve;
            case RagdollSlots.Player.LEFT_LEG -> modelRoot.leftPants;
            case RagdollSlots.Player.RIGHT_LEG -> modelRoot.rightPants;
            default -> null;
        };
    }
}