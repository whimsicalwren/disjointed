package dev.wren.disjointed.mixin;

import dev.wren.disjointed.bodies.EntityVsBodyExtension;
import dev.wren.disjointed.bodies.PhysTickHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.valkyrienskies.core.api.bodies.ServerVsBody;
import org.valkyrienskies.core.api.bodies.properties.BodyPose;

import static dev.wren.disjointed.util.Utils.createBodyPose;
import static dev.wren.disjointed.util.Utils.getActualPosition;

@Mixin(Entity.class)
public class EntityVsBodyMixin implements EntityVsBodyExtension {

    @Shadow private Vec3 position;
    @Shadow private AABB bb;

    @Unique private ServerVsBody body;


    @Override
    public AABB getBoundingBox() {
        return bb;
    }

    @Override
    public Vector3d getPosition() {
        return new Vector3d(position.x, position.y + (bb.getYsize() / 2), position.z);
    }

    @Override
    public ServerVsBody getBody() {
        return body;
    }

    @Override
    public void setBody(@NotNull ServerVsBody body) {
        this.body = body;
        System.out.println("set to " + body.getId());
        PhysTickHandler.LINKED_BODIES.add(this);
    }

    @Override
    public void removeBody() {
        System.out.println("removing " + body.getId());
        this.body = null;
        PhysTickHandler.LINKED_BODIES.remove(this);
    }

    @Override
    public Long getBodyId() {
        return this.body.getId();
    }
}
