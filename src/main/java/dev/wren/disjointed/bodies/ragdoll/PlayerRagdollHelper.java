package dev.wren.disjointed.bodies.ragdoll;

import dev.wren.disjointed.bodies.ragdoll.group.PlayerRagdollGroup;
import net.minecraft.server.level.ServerLevel;
import org.joml.Vector3d;

public class PlayerRagdollHelper {

    public static PlayerRagdollGroup createRagdollClassic(ServerLevel level, Vector3d pos, String username) {
        //noinspection DuplicatedCode
        PlayerRagdollGroup group = PlayerRagdollGroup.create(level, pos, username);

        group.createJoints(level);

        RagdollManager.get(level).addRagdoll(group);

        return group;
    }
}
