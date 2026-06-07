package dev.wren.disjointed.bodies.ragdoll;

import net.minecraft.server.level.ServerLevel;
import org.joml.Matrix3d;
import org.joml.Vector3d;
import org.valkyrienskies.core.api.bodies.VsBodyCreateData;
import org.valkyrienskies.core.api.bodies.VsBodyDefaults;
import org.valkyrienskies.core.api.bodies.shape.BodyShapeData;
import org.valkyrienskies.core.api.bodies.shape.BoxBodyShapeData;
import org.valkyrienskies.core.impl.bodies.properties.BodyKinematicsImpl;
import org.valkyrienskies.core.impl.bodies.properties.BodyTransformImpl;
import org.valkyrienskies.core.impl.game.bodies.BodyInertiaDataImpl;
import org.valkyrienskies.core.internal.world.VsiServerShipWorld;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import static dev.wren.disjointed.util.Utils.*;

public class PlayerRagdollHelper {

    public static PlayerRagdollGroup createRagdollClassic(ServerLevel level, Vector3d pos) {
        //noinspection DuplicatedCode
        PlayerRagdollGroup group = new PlayerRagdollGroup();
        VsiServerShipWorld shipWorld = VSGameUtilsKt.getShipObjectWorld(level);
        group.addSlot(PlayerRagdollGroup.HEAD_SLOT, shipWorld.createBody(createRagdollPieceData(level, headShape(), new Vector3d(pos).add(0, pxToBlocks(10), 0), 1000)));
        group.addSlot(PlayerRagdollGroup.TORSO_SLOT, shipWorld.createBody(createRagdollPieceData(level, torsoShape(), new Vector3d(pos), 1000)));
        // arms
        group.addSlot(PlayerRagdollGroup.LEFT_ARM_SLOT, shipWorld.createBody(createRagdollPieceData(level, armOrLegShape(), new Vector3d(pos).add(pxToBlocks(-6), 0, 0), 1000)));
        group.addSlot(PlayerRagdollGroup.RIGHT_ARM_SLOT, shipWorld.createBody(createRagdollPieceData(level, armOrLegShape(), new Vector3d(pos).add(pxToBlocks(6), 0, 0), 1000)));
        // legs
        //noinspection DuplicatedCode
        group.addSlot(PlayerRagdollGroup.LEFT_LEG_SLOT, shipWorld.createBody(createRagdollPieceData(level, armOrLegShape(), new Vector3d(pos).add(pxToBlocks(-2), pxToBlocks(-12), 0), 1000)));
        group.addSlot(PlayerRagdollGroup.RIGHT_LEG_SLOT, shipWorld.createBody(createRagdollPieceData(level, armOrLegShape(), new Vector3d(pos).add(pxToBlocks(2), pxToBlocks(-12), 0), 1000)));

        return group;
    }

    public static PlayerRagdollGroup createRagdollSlim(ServerLevel level, Vector3d pos) {
        //noinspection DuplicatedCode go away
        PlayerRagdollGroup   group = new PlayerRagdollGroup();
        VsiServerShipWorld shipWorld = VSGameUtilsKt.getShipObjectWorld(level);
        group.addSlot(PlayerRagdollGroup.HEAD_SLOT, shipWorld.createBody(createRagdollPieceData(level, headShape(), new Vector3d(pos).add(0, 0.5625, 0), 1000)));
        group.addSlot(PlayerRagdollGroup.TORSO_SLOT, shipWorld.createBody(createRagdollPieceData(level, torsoShape(), new Vector3d(pos), 1000)));
        // arms
        group.addSlot(PlayerRagdollGroup.LEFT_ARM_SLOT, shipWorld.createBody(createRagdollPieceData(level, slimArmShape(), new Vector3d(pos).add(0, 0.1875, 0), 1000)));
        group.addSlot(PlayerRagdollGroup.RIGHT_ARM_SLOT, shipWorld.createBody(createRagdollPieceData(level, slimArmShape(), new Vector3d(pos).add(0, 0.1875, 0), 1000)));
        // legs
        //noinspection DuplicatedCode
        group.addSlot(PlayerRagdollGroup.LEFT_LEG_SLOT, shipWorld.createBody(createRagdollPieceData(level, armOrLegShape(), new Vector3d(pos).add(0.125, -0.75, 0), 1000)));
        group.addSlot(PlayerRagdollGroup.RIGHT_LEG_SLOT, shipWorld.createBody(createRagdollPieceData(level, armOrLegShape(), new Vector3d(pos).add(-0.125, -0.75, 0), 1000)));

        return group;
    }

    public static VsBodyCreateData createRagdollPieceData(ServerLevel level, BodyShapeData shapeData, Vector3d pos, double mass) {
        return new VsBodyCreateData(
                VSGameUtilsKt.getDimensionId(level),
                new BodyInertiaDataImpl(shapeData.getAabb().center(new Vector3d()), mass, new Matrix3d()),
                new BodyKinematicsImpl(all(0), all(0), new BodyTransformImpl(pos, unitQuaternion(), all(1), all(0))),
                shapeData,
                true,
                VsBodyDefaults.DEFAULT_COLLISION_MASK,
                VsBodyDefaults.DEFAULT_STATIC_FRICTION_COEFFICIENT,
                VsBodyDefaults.DEFAULT_DYNAMIC_FRICTION_COEFFICIENT,
                VsBodyDefaults.DEFAULT_RESTITUTION_COEFFICIENT
        );
    }

    public static BodyShapeData headShape() {
        return new BoxBodyShapeData(all(0.5));
    }

    public static BodyShapeData torsoShape() {
        return new BoxBodyShapeData(new Vector3d(0.5, 0.75, 0.25));
    }

    public static BodyShapeData armOrLegShape() {
        return new BoxBodyShapeData(new Vector3d(0.25, 0.75, 0.25));
    }

    public static BodyShapeData slimArmShape() {
        return new BoxBodyShapeData(new Vector3d(0.1875, 0.75, 0.25));
    }



}
