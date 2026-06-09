package dev.wren.disjointed.bodies.ragdoll;

import dev.wren.disjointed.bodies.ragdoll.client.ClientRagdollRenderer;
import dev.wren.disjointed.bodies.ragdoll.client.renderer.PlayerRagdollRenderer;
import dev.wren.disjointed.bodies.ragdoll.client.renderer.SlimPlayerRagdollRenderer;
import dev.wren.disjointed.bodies.ragdoll.types.PlayerRagdoll;
import dev.wren.disjointed.bodies.ragdoll.types.SlimPlayerRagdoll;
import dev.wren.disjointed.util.Utils;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class RagdollRegistry {
    private static final Map<String, RagdollSpec> REGISTRY = new HashMap<>();

    public static void register(String type, ClientRagdollRenderer renderer, Function<CompoundTag, Ragdoll> deserializer, RagdollFactory factory) {
        REGISTRY.put(type, new RagdollSpec(renderer, deserializer, factory));
    }

    public static Ragdoll deserialize(CompoundTag nbt) {
        String id = nbt.getString("type");
        RagdollSpec spec = REGISTRY.get(id);
        if (spec == null || spec.deserializer == null) throw new IllegalArgumentException("Unknown ragdoll type: " + id);

        return spec.deserializer.apply(nbt);
    }

    public static ClientRagdollRenderer getRenderer(String typeId) {
        RagdollSpec spec = REGISTRY.get(typeId);
        return spec == null ? ClientRagdollRenderer.NONE : spec.renderer;
    }

    public static RagdollFactory getFactory(String typeId) {
        RagdollSpec spec = REGISTRY.get(typeId);
        return spec == null ? null : spec.factory;
    }

    public static RagdollSpec get(String typeId) {
        return REGISTRY.get(typeId);
    }

    public static boolean isValidType(String typeId) {
        return REGISTRY.containsKey(typeId);
    }

    public static Set<String> getTypes() {
        return REGISTRY.keySet();
    }

    public static void init() {
        register("player", new PlayerRagdollRenderer(), tag -> {
            UUID uuid = tag.getUUID("uuid");
            String username = tag.getString("username");

            HashMap<String, Long> ids = Utils.readMap(tag.getCompound("pieces"), RagdollSlots.Player.allSlots(), CompoundTag::getLong);

            return new PlayerRagdoll(uuid, username, ids);
        }, ((level, pos, args, isStatic) -> PlayerRagdoll.create(level, pos, args.get(0), isStatic)));

        register("player_slim", new SlimPlayerRagdollRenderer(), tag -> {
            UUID uuid = tag.getUUID("uuid");
            String username = tag.getString("username");

            HashMap<String, Long> ids = Utils.readMap(tag.getCompound("pieces"), RagdollSlots.Player.allSlots(), CompoundTag::getLong);

            return new SlimPlayerRagdoll(uuid, username, ids);
        }, ((level, pos, args, isStatic) -> SlimPlayerRagdoll.create(level, pos, args.get(0), isStatic)));
    }

    public record RagdollSpec(ClientRagdollRenderer renderer, Function<CompoundTag, Ragdoll> deserializer, RagdollFactory factory) {}

}
