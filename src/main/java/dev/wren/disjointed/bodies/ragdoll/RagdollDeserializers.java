package dev.wren.disjointed.bodies.ragdoll;

import dev.wren.disjointed.bodies.ragdoll.group.PlayerRagdollGroup;
import dev.wren.disjointed.bodies.ragdoll.group.RagdollGroupRegistry;
import dev.wren.disjointed.util.Utils;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.UUID;

public class RagdollDeserializers {

    public static void register() {
        RagdollGroupRegistry.register("player", (tag -> {
            UUID uuid = tag.getUUID("uuid");
            String username = tag.getString("username");

            HashMap<String, Long> ids = Utils.readMap(tag.getCompound("pieces"), RagdollSlots.Player.allSlots(), CompoundTag::getLong);

            return new PlayerRagdollGroup(uuid, username, ids);
        }));
    }

}
