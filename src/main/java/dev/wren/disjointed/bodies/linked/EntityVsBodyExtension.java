package dev.wren.disjointed.bodies.linked;

import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.valkyrienskies.core.api.bodies.ServerVsBody;

import static dev.wren.disjointed.util.Utils.createBodyPose;


public interface EntityVsBodyExtension {
    ServerVsBody getBody();
    void setBody(@NotNull ServerVsBody body);
    void removeBody();
    Long getBodyId();

    Vector3d getPosition();
    AABB getBoundingBox();

    default boolean hasBody() {
        return getBody() != null;
    }
}
