package dev.wren.disjointed.bodies.ragdoll.client.renderer;

import dev.wren.disjointed.bodies.ragdoll.RagdollSlots;
import dev.wren.disjointed.bodies.ragdoll.client.ClientRagdoll;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3d;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import static dev.wren.disjointed.bodies.ragdoll.RagdollSlots.EnderDragon.*;

public class EnderDragonRagdollRenderer extends BaseRagdollRenderer<EnderDragonRenderer.DragonModel> {

    protected final Map<UUID, EnderDragonRenderer.DragonModel> models = new HashMap<>();

    @Override
    protected EnderDragonRenderer.DragonModel getOrBakeModel(UUID uuid) {
        return models.computeIfAbsent(uuid, id -> {
            ModelPart root = Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.ENDER_DRAGON);
            return new EnderDragonRenderer.DragonModel(root);
        });
    }

    @Override
    protected ResourceLocation getOrLoadTexture(ClientRagdoll ragdoll) {
        return ResourceLocation.withDefaultNamespace("textures/entity/enderdragon/dragon.png");
    }

    @Override
    protected ModelPart getModelPartForSlot(String slot, EnderDragonRenderer.DragonModel modelRoot) {
        return switch (slot) {
            case LOWER_MOUTH -> modelRoot.jaw;
            case UPPER_MOUTH, HEAD -> modelRoot.head;
            case BODY -> modelRoot.body;
            case LEFT_WING -> modelRoot.leftWing;
            case LEFT_WING_TIP -> modelRoot.leftWingTip;
            case RIGHT_WING -> modelRoot.rightWing;
            case RIGHT_WING_TIP -> modelRoot.rightWingTip;
            case LEFT_FRONT_LEG -> modelRoot.leftFrontLeg;
            case LEFT_FRONT_LEG_TIP -> modelRoot.leftFrontLegTip;
            case LEFT_FRONT_FOOT -> modelRoot.leftFrontFoot;
            case LEFT_REAR_LEG -> modelRoot.leftRearLeg;
            case LEFT_REAR_LEG_TIP -> modelRoot.leftRearLegTip;
            case LEFT_REAR_FOOT -> modelRoot.leftRearFoot;
            case RIGHT_FRONT_LEG -> modelRoot.rightFrontLeg;
            case RIGHT_FRONT_LEG_TIP -> modelRoot.rightFrontLegTip;
            case RIGHT_FRONT_FOOT -> modelRoot.rightFrontFoot;
            case RIGHT_REAR_LEG -> modelRoot.rightRearLeg;
            case RIGHT_REAR_LEG_TIP -> modelRoot.rightRearLegTip;
            case RIGHT_REAR_FOOT -> modelRoot.rightRearFoot;
            default -> modelRoot.neck;
        };
    }

    @Override
    protected Vector3d getOffsetVector(String slot) {
        return null;
    }
}
