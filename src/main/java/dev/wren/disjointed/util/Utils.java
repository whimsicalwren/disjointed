package dev.wren.disjointed.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.valkyrienskies.core.api.bodies.properties.BodyPose;

import java.util.Collection;
import java.util.List;

public class Utils {

    public static Vector3d fromVec3(Vec3 vec3) {
        return new Vector3d(vec3.x, vec3.y, vec3.z);
    }

    public static Vector3d all(double d) {
        return new Vector3d(d, d, d);
    }

    public static Quaterniond unitQuaternion() {
        return new Quaterniond(0, 0, 0, 1);
    }

    public static Vector3d getActualPosition(Entity entity) {
        Vector3d pos = fromVec3(entity.position());
        return new Vector3d(pos.x, pos.y + (entity.getBoundingBox().getYsize() / 2), pos.z);
    }

    public static BodyPose createBodyPose(Vec3 position, AABB boundingBox) {
        return new BodyPose() {
            @Override
            public @NotNull Vector3dc getPosition() {
                return new Vector3d(position.x, position.y + (boundingBox.getYsize() / 2), position.z);
            }

            @Override
            public @NotNull Quaterniondc getRotation() {
                return unitQuaternion();
            }
        };
    }

    public static String listAsString(List<?> list) {
        StringBuilder builder = new StringBuilder("[");
        for (Object o : list) {
            builder.append(o.toString());
            builder.append(list.indexOf(o) == list.size() - 1 ? "]" : ", ");
        }
        return builder.toString();
    }

    public static float pxToBlocks(float pixels) {
        return pixels / 16f;
    }

}
