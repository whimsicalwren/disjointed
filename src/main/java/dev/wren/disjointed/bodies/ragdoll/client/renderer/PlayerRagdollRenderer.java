package dev.wren.disjointed.bodies.ragdoll.client.renderer;

import dev.wren.disjointed.bodies.ragdoll.RagdollSlots;
import dev.wren.disjointed.bodies.ragdoll.client.ClientRagdoll;
import dev.wren.disjointed.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3d;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static dev.wren.disjointed.util.Utils.pxToBlocks;

public class PlayerRagdollRenderer extends BaseRagdollRenderer<PlayerModel<AbstractClientPlayer>, RagdollSlots.Humanoid> {

    protected final Map<UUID, PlayerModel<AbstractClientPlayer>> models = new HashMap<>();
    protected final Map<UUID, ResourceLocation> textures = new HashMap<>();

    @Override
    protected PlayerModel<AbstractClientPlayer> getOrBakeModel(UUID uuid) {
        return models.computeIfAbsent(uuid, id -> {
            ModelPart root = Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER);
            return new PlayerModel<>(root, false);
        });
    }

    @Override
    protected ResourceLocation getOrLoadTexture(ClientRagdoll<RagdollSlots.Humanoid> ragdoll) {
        if (textures.containsKey(ragdoll.uuid()))
            return textures.get(ragdoll.uuid());

        textures.put(ragdoll.uuid(), DefaultPlayerSkin.getDefaultSkin());

        String username = ragdoll.extraData().getString("username");
        if (!username.isEmpty()) {
            Utils.getSkin(username, skin -> textures.put(ragdoll.uuid(), skin));
        }

        return textures.get(ragdoll.uuid());
    }

    @Override
    protected ModelPart getModelPartForSlot(RagdollSlots.Humanoid slot, PlayerModel<AbstractClientPlayer> modelRoot) {
        return switch (slot) {
            case HEAD -> modelRoot.head;
            case TORSO -> modelRoot.body;
            case LEFT_ARM -> modelRoot.leftArm;
            case RIGHT_ARM -> modelRoot.rightArm;
            case LEFT_LEG -> modelRoot.leftLeg;
            case RIGHT_LEG -> modelRoot.rightLeg;
        };
    }

    @Override
    protected ModelPart getModelLayerPartForSlot(RagdollSlots.Humanoid slot, PlayerModel<AbstractClientPlayer> modelRoot) {
        return switch (slot) {
            case HEAD -> modelRoot.hat;
            case TORSO -> modelRoot.jacket;
            case LEFT_ARM -> modelRoot.leftSleeve;
            case RIGHT_ARM -> modelRoot.rightSleeve;
            case LEFT_LEG -> modelRoot.leftPants;
            case RIGHT_LEG -> modelRoot.rightPants;
        };
    }

    @Override
    protected Vector3d getOffsetVector(RagdollSlots.Humanoid slot) {
        return switch (slot) {
            case HEAD      -> new Vector3d(0, pxToBlocks(-4), 0);
            case TORSO     -> new Vector3d(0, 0.375f, 0);
            case LEFT_ARM -> new Vector3d(pxToBlocks(6), 0.375f, 0);
            case RIGHT_ARM -> new Vector3d(pxToBlocks(-6), 0.375f, 0);
            case LEFT_LEG -> new Vector3d(pxToBlocks(2), pxToBlocks(18), 0);
            case RIGHT_LEG -> new Vector3d(pxToBlocks(-2), pxToBlocks(18), 0);
        };
    }
}