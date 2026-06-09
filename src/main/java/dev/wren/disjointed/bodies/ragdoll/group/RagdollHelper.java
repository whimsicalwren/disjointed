package dev.wren.disjointed.bodies.ragdoll.group;

import dev.wren.disjointed.bodies.ragdoll.RagdollFactory;
import dev.wren.disjointed.bodies.ragdoll.RagdollManager;
import dev.wren.disjointed.bodies.ragdoll.RagdollRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.valkyrienskies.mod.common.ValkyrienSkiesMod;

import java.util.List;

public class RagdollHelper {

    public static int createRagdoll(ServerLevel level, Vec3 pos, String type, String argsString, boolean isStatic) {
        List<String> args = List.of(argsString.split(","));

        RagdollFactory factory = RagdollRegistry.getFactory(type);
        if (factory == null) return 0;

        Ragdoll ragdoll = factory.create(level, new Vector3d(pos.x, pos.y, pos.z), args, isStatic);

        ragdoll.createJoints(level);

        ValkyrienSkiesMod.getApi().getPhysTickEvent().on(((event, registeredListener) -> {
            ragdoll.changeCollision(event.getWorld());
            registeredListener.unregister();
        }));

        RagdollManager.get(level).addRagdoll(ragdoll);
        return 1;
    }

}
