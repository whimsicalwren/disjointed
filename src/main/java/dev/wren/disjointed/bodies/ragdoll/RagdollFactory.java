package dev.wren.disjointed.bodies.ragdoll;

import dev.wren.disjointed.bodies.ragdoll.group.Ragdoll;
import net.minecraft.server.level.ServerLevel;
import org.joml.Vector3d;

import java.util.List;

@FunctionalInterface
public interface RagdollFactory {
    Ragdoll create(ServerLevel level, Vector3d pos, List<String> args, boolean isStatic);
}