package dev.wren.disjointed.bodies.ragdoll;

import net.minecraft.server.level.ServerLevel;
import org.joml.Matrix3d;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.valkyrienskies.core.api.bodies.VsBodyCreateData;
import org.valkyrienskies.core.api.bodies.VsBodyDefaults;
import org.valkyrienskies.core.api.bodies.shape.BodyShapeData;
import org.valkyrienskies.core.api.bodies.shape.BoxBodyShapeData;
import org.valkyrienskies.core.impl.bodies.properties.BodyKinematicsImpl;
import org.valkyrienskies.core.impl.bodies.properties.BodyTransformImpl;
import org.valkyrienskies.core.impl.game.bodies.BodyInertiaDataImpl;
import org.valkyrienskies.core.internal.joints.VSD6Joint;
import org.valkyrienskies.core.internal.joints.VSJoint;
import org.valkyrienskies.core.internal.joints.VSJointPose;
import org.valkyrienskies.core.internal.joints.VSSphericalJoint;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import static dev.wren.disjointed.util.Utils.all;

public class RagdollUtils {

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
        return createRagdollJoint(mainId, pieceId, mainPos, piecePos, null);
    }

    public static VSSphericalJoint createRagdollJoint(Long mainId, Long pieceId, Vector3d mainPos, Vector3d piecePos, float y, float z) {
        return createRagdollJoint(mainId, pieceId, mainPos, piecePos, createLimitCone(y, z));
    }

    public static VSSphericalJoint createRagdollJoint(Long mainId, Long pieceId, Vector3d mainPos, Vector3d piecePos, VSD6Joint.LimitCone limitCone) {
        return (VSSphericalJoint) new VSSphericalJoint(
                mainId, new VSJointPose(mainPos, new Quaterniond()),
                pieceId, new VSJointPose(piecePos, new Quaterniond()),
                null, VSJoint.DEFAULT_COMPLIANCE, limitCone
        ).serialized();
    }
    public static VSD6Joint.LimitCone createLimitCone(float y, float z) {
        return new VSD6Joint.LimitCone(y, z, null, 0.1f, 10f, null);
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
