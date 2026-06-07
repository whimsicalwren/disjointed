package dev.wren.disjointed.bodies.ragdoll.client;

import net.minecraft.nbt.CompoundTag;
import org.valkyrienskies.core.api.bodies.ClientVsBody;

import java.util.Map;
import java.util.UUID;

public record ClientRagdoll(UUID uuid, String typeId, Map<String, ClientVsBody> slots, CompoundTag extraData) {
}
