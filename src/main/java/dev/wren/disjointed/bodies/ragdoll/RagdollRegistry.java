package dev.wren.disjointed.bodies.ragdoll;

import dev.wren.disjointed.bodies.ragdoll.client.ClientRagdollRenderer;
import dev.wren.disjointed.bodies.ragdoll.client.renderer.PlayerRagdollRenderer;
import dev.wren.disjointed.bodies.ragdoll.client.renderer.SlimPlayerRagdollRenderer;
import dev.wren.disjointed.bodies.ragdoll.group.PlayerRagdollGroup;
import dev.wren.disjointed.bodies.ragdoll.group.RagdollGroup;
import dev.wren.disjointed.util.Utils;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class RagdollRegistry {
    private static final Map<String, ClientRagdollRenderer> RENDERERS = new HashMap<>();
    private static final Map<String, Function<CompoundTag, RagdollGroup>> DESERIALIZERS = new HashMap<>();

    public static void register(String type, ClientRagdollRenderer renderer, Function<CompoundTag, RagdollGroup> deserializer) {
        RENDERERS.put(type, renderer);
        DESERIALIZERS.put(type, deserializer);
    }

    public static RagdollGroup deserialize(CompoundTag nbt) {
        String id = nbt.getString("type");
        Function<CompoundTag, RagdollGroup> deserializer = DESERIALIZERS.get(id);
        if (deserializer == null) throw new IllegalArgumentException("Unknown ragdoll type: " + id);

        return deserializer.apply(nbt);
    }

    public static ClientRagdollRenderer getRenderer(String typeId) {
        return RENDERERS.getOrDefault(typeId, ClientRagdollRenderer.NONE);
    }

    public static void init() {
        register("player", new PlayerRagdollRenderer(), tag -> {
            UUID uuid = tag.getUUID("uuid");
            String username = tag.getString("username");

            HashMap<String, Long> ids = Utils.readMap(tag.getCompound("pieces"), RagdollSlots.Player.allSlots(), CompoundTag::getLong);

            return new PlayerRagdollGroup(uuid, username, ids);
        });

        register("player_slim", new SlimPlayerRagdollRenderer(), tag -> {
            UUID uuid = tag.getUUID("uuid");
            String username = tag.getString("username");

            HashMap<String, Long> ids = Utils.readMap(tag.getCompound("pieces"), RagdollSlots.Player.allSlots(), CompoundTag::getLong);

            return new PlayerRagdollGroup(uuid, username, ids);
        });
    }

}
