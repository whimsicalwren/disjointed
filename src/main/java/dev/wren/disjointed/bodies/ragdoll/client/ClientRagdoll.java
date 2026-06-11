package dev.wren.disjointed.bodies.ragdoll.client;

import net.minecraft.nbt.CompoundTag;
import org.valkyrienskies.core.api.bodies.ClientVsBody;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public record ClientRagdoll<E extends Enum<E>>(UUID uuid, String typeId, EnumMap<E, Long> pieces, CompoundTag extraData) {}
