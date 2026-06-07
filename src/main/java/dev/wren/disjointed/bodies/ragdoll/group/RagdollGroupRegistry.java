package dev.wren.disjointed.bodies.ragdoll.group;

import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class RagdollGroupRegistry {
    private static final Map<String, Function<CompoundTag, RagdollGroup>> REGISTRY = new HashMap<>();

    public static void register(String id, Function<CompoundTag, RagdollGroup> deserializer) {
        REGISTRY.put(id, deserializer);
    }

    public static RagdollGroup deserialize(CompoundTag nbt) {
        String id = nbt.getString("type");
        Function<CompoundTag, RagdollGroup> deserializer = REGISTRY.get(id);
        if (deserializer == null) throw new IllegalArgumentException("Unknown ragdoll type: " + id);

        return deserializer.apply(nbt);
    }
}