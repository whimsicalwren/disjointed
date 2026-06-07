package dev.wren.disjointed.bodies.ragdoll;

import dev.wren.disjointed.DisjointedNetwork;
import dev.wren.disjointed.bodies.ragdoll.packet.AddRagdollPacket;
import dev.wren.disjointed.bodies.ragdoll.group.PlayerRagdollGroup;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.joml.Matrix3d;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.valkyrienskies.core.api.bodies.ServerVsBody;
import org.valkyrienskies.core.api.bodies.VsBodyCreateData;
import org.valkyrienskies.core.api.bodies.VsBodyDefaults;
import org.valkyrienskies.core.api.bodies.shape.BodyShapeData;
import org.valkyrienskies.core.api.bodies.shape.BoxBodyShapeData;
import org.valkyrienskies.core.impl.bodies.properties.BodyKinematicsImpl;
import org.valkyrienskies.core.impl.bodies.properties.BodyTransformImpl;
import org.valkyrienskies.core.impl.game.bodies.BodyInertiaDataImpl;
import org.valkyrienskies.core.internal.joints.VSJoint;
import org.valkyrienskies.core.internal.joints.VSJointPose;
import org.valkyrienskies.core.internal.joints.VSSphericalJoint;
import org.valkyrienskies.core.internal.world.VsiServerShipWorld;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.ValkyrienSkiesMod;
import org.valkyrienskies.mod.common.util.GameToPhysicsAdapter;

import static dev.wren.disjointed.util.Utils.*;

public class PlayerRagdollHelper {

    public static PlayerRagdollGroup createPlayerRagdoll(ServerPlayer player, Vector3d pos, String username) {
        PlayerRagdollGroup playerRagdoll = createRagdollClassic(player.serverLevel(), pos, username);

        //RagdollManager.syncAllRagdollsToPlayer(player);

        return playerRagdoll;
    }

    public static PlayerRagdollGroup createRagdollClassic(ServerLevel level, Vector3d pos, String username) {
        //noinspection DuplicatedCode
        PlayerRagdollGroup group = new PlayerRagdollGroup(username);
        VsiServerShipWorld shipWorld = VSGameUtilsKt.getShipObjectWorld(level);
        ServerVsBody head = group.addSlot(RagdollSlots.Player.HEAD, shipWorld.createBody(createRagdollPieceData(level, headShape(), new Vector3d(pos).add(0, pxToBlocks(10), 0), 1000)));
        ServerVsBody torso = group.addSlot(RagdollSlots.Player.TORSO, shipWorld.createBody(createRagdollPieceData(level, torsoShape(), new Vector3d(pos), 1000)));
        // arms
        ServerVsBody leftArm = group.addSlot(RagdollSlots.Player.LEFT_ARM, shipWorld.createBody(createRagdollPieceData(level, armOrLegShape(), new Vector3d(pos).add(pxToBlocks(6), 0, 0), 1000)));
        ServerVsBody rightArm = group.addSlot(RagdollSlots.Player.RIGHT_ARM, shipWorld.createBody(createRagdollPieceData(level, armOrLegShape(), new Vector3d(pos).add(pxToBlocks(-6), 0, 0), 1000)));
        // legs
        //noinspection DuplicatedCode
        ServerVsBody leftLeg = group.addSlot(RagdollSlots.Player.LEFT_LEG, shipWorld.createBody(createRagdollPieceData(level, armOrLegShape(), new Vector3d(pos).add(pxToBlocks(2), pxToBlocks(-12), 0), 1000)));
        ServerVsBody rightLeg = group.addSlot(RagdollSlots.Player.RIGHT_LEG, shipWorld.createBody(createRagdollPieceData(level, armOrLegShape(), new Vector3d(pos).add(pxToBlocks(-2), pxToBlocks(-12), 0), 1000)));

        GameToPhysicsAdapter gtpa = ValkyrienSkiesMod.getOrCreateGTPA(VSGameUtilsKt.getDimensionId(level));

        VSSphericalJoint torsoToHead = createRagdollJoint(torso.getId(), head.getId(), new Vector3d(0, pxToBlocks(6), 0), new Vector3d(0, pxToBlocks(-4), 0));
        VSSphericalJoint torsoToLeftArm = createRagdollJoint(torso.getId(), leftArm.getId(), new Vector3d(pxToBlocks(4), pxToBlocks(6), 0), new Vector3d(pxToBlocks(-2), pxToBlocks(6), 0));
        VSSphericalJoint torsoToRightArm = createRagdollJoint(torso.getId(), rightArm.getId(), new Vector3d(pxToBlocks(-4), pxToBlocks(6), 0), new Vector3d(pxToBlocks(2), pxToBlocks(6), 0));
        VSSphericalJoint torsoToLeftLeg = createRagdollJoint(torso.getId(), leftLeg.getId(), new Vector3d(pxToBlocks(2), pxToBlocks(-6), 0), new Vector3d(0, pxToBlocks(6), 0));
        VSSphericalJoint torsoToRightLeg = createRagdollJoint(torso.getId(), rightLeg.getId(), new Vector3d(pxToBlocks(-2), pxToBlocks(-6), 0), new Vector3d(0, pxToBlocks(6), 0));

        gtpa.addJoint(torsoToHead, 5, id -> {});
        gtpa.addJoint(torsoToLeftArm, 5, id -> {});
        gtpa.addJoint(torsoToRightArm, 5, id -> {});
        gtpa.addJoint(torsoToLeftLeg, 5, id -> {});
        gtpa.addJoint(torsoToRightLeg, 5, id -> {});

        RagdollManager.get(level).addRagdoll(group);

        return group;
    }

    public static VsBodyCreateData createRagdollPieceData(ServerLevel level, BodyShapeData shapeData, Vector3d pos, double mass) {
        return new VsBodyCreateData(
                VSGameUtilsKt.getDimensionId(level),
                new BodyInertiaDataImpl(shapeData.getAabb().center(new Vector3d()), mass, new Matrix3d()),
                new BodyKinematicsImpl(all(0), all(0), new BodyTransformImpl(pos, new Quaterniond(), all(1), all(0))),
                shapeData,
                false,
                VsBodyDefaults.DEFAULT_COLLISION_MASK,
                VsBodyDefaults.DEFAULT_STATIC_FRICTION_COEFFICIENT,
                VsBodyDefaults.DEFAULT_DYNAMIC_FRICTION_COEFFICIENT,
                VsBodyDefaults.DEFAULT_RESTITUTION_COEFFICIENT
        );
    }

    public static VSSphericalJoint createRagdollJoint(Long mainId, Long pieceId, Vector3d mainPos, Vector3d piecePos) {
        return (VSSphericalJoint) new VSSphericalJoint(
                mainId, new VSJointPose(mainPos, new Quaterniond()),
                pieceId, new VSJointPose(piecePos, new Quaterniond()),
                null, VSJoint.DEFAULT_COMPLIANCE, null
        ).serialized();
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
