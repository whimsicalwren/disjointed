package dev.wren.disjointed.bodies.ragdoll;

import net.minecraft.server.level.ServerLevel;
import org.joml.Vector3d;

import java.util.List;

@FunctionalInterface
public interface RagdollFactory<E extends Enum<E>> {
    Ragdoll<E> create(ServerLevel level, Vector3d pos, List<String> args, boolean isStatic);
}