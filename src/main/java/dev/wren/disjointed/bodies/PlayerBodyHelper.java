package dev.wren.disjointed.bodies;

import dev.wren.disjointed.Disjointed;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
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
import org.valkyrienskies.mod.common.VSGameUtilsKt;

public class PlayerBodyHelper {

    public static ServerVsBody createBody(ServerLevel level, ServerPlayer player) {
        try {
            Vector3d pos = fromVec3(player.position());
            Vector3d posMid = new Vector3d(pos.x, pos.y + (player.getBoundingBox().getYsize() / 2), pos.z);
            BodyShapeData shapeData = createShapeDataHalf(player);

            VsBodyCreateData bodyCreateData = new VsBodyCreateData(
                    VSGameUtilsKt.getDimensionId(level),
                    new BodyInertiaDataImpl(shapeData.getAabb().center(new Vector3d(posMid)), 1000, new Matrix3d()),
                    new BodyKinematicsImpl(zero(), zero(), new BodyTransformImpl(posMid, new Quaterniond(0, 0, 0, 1), new Vector3d(1, 1, 1), zero())),
                    shapeData,
                    false,
                    VsBodyDefaults.DEFAULT_COLLISION_MASK,
                    VsBodyDefaults.DEFAULT_STATIC_FRICTION_COEFFICIENT,
                    VsBodyDefaults.DEFAULT_DYNAMIC_FRICTION_COEFFICIENT,
                    VsBodyDefaults.DEFAULT_RESTITUTION_COEFFICIENT
            );
            return VSGameUtilsKt.getShipObjectWorld(level).createBody(bodyCreateData);
        } catch (Exception e) {
            Disjointed.LOGGER.error("Error while trying to create body for player {}: {}", player.getName().getString(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static BodyShapeData createShapeData(Player player) {
        AABB playerAABB = player.getBoundingBox();
        return new BoxBodyShapeData(new Vector3d(playerAABB.getXsize(), playerAABB.getYsize(), playerAABB.getZsize()));
    }

    private static BodyShapeData createShapeDataHalf(Player player) {
        AABB playerAABB = player.getBoundingBox();
        return new BoxBodyShapeData(new Vector3d(playerAABB.getXsize(), playerAABB.getYsize() / 2, playerAABB.getZsize()));
    }

    private static Vector3d fromVec3(Vec3 vec3) {
        return new Vector3d(vec3.x, vec3.y, vec3.z);
    }

    private static Vector3d zero() {
        return new Vector3d(0,0,0);
    }
}
