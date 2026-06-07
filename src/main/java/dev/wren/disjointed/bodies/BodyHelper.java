package dev.wren.disjointed.bodies;

import dev.wren.disjointed.Disjointed;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
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

import static dev.wren.disjointed.util.Utils.*;

public class BodyHelper {

    public static ServerVsBody createBody(ServerLevel level, Entity entity) {
        try {
            Vector3d posMid = getActualPosition(entity);
            BodyShapeData shapeData = createShapeData(entity);

            VsBodyCreateData bodyCreateData = new VsBodyCreateData(
                    VSGameUtilsKt.getDimensionId(level),
                    new BodyInertiaDataImpl(shapeData.getAabb().center(new Vector3d(posMid)), 1000, new Matrix3d()),
                    new BodyKinematicsImpl(all(0), all(0), new BodyTransformImpl(posMid, new Quaterniond(), all(1), all(0))),
                    shapeData,
                    false,
                    VsBodyDefaults.DEFAULT_COLLISION_MASK,
                    VsBodyDefaults.DEFAULT_STATIC_FRICTION_COEFFICIENT,
                    VsBodyDefaults.DEFAULT_DYNAMIC_FRICTION_COEFFICIENT,
                    VsBodyDefaults.DEFAULT_RESTITUTION_COEFFICIENT
            );
            ServerVsBody body = VSGameUtilsKt.getShipObjectWorld(level).createBody(bodyCreateData);
            ((EntityVsBodyExtension) entity).setBody(body);
            return body;
        } catch (Exception e) {
            Disjointed.LOGGER.error("Error while trying to create body for entity {}: {}", entity.getName().getString(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static BodyShapeData createShapeData(Entity entity) {
        AABB bounds = entity.getBoundingBox();
        return new BoxBodyShapeData(new Vector3d(bounds.getXsize(), bounds.getYsize(), bounds.getZsize()));
    }
}
