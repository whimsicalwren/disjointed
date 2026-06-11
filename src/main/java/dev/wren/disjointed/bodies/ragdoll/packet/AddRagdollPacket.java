package dev.wren.disjointed.bodies.ragdoll.packet;

import dev.wren.disjointed.bodies.ragdoll.RagdollRegistry;
import dev.wren.disjointed.bodies.ragdoll.client.ClientRagdoll;
import dev.wren.disjointed.bodies.ragdoll.client.ClientRagdollManager;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.valkyrienskies.core.api.bodies.ClientVsBody;
import org.valkyrienskies.core.api.bodies.QueryableVsBodyData;
import org.valkyrienskies.core.api.bodies.VsBody;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class AddRagdollPacket<E extends Enum<E>> {
    private final UUID uuid;
    private final String typeId;
    private final EnumMap<E, Long> pieces;
    private final CompoundTag extraData;

    public AddRagdollPacket(UUID uuid, String typeId, EnumMap<E, Long> pieces, CompoundTag extraData) {
        this.uuid = uuid;
        this.typeId = typeId;
        this.pieces = pieces;
        this.extraData = extraData;
    }

    public static <E extends Enum<E>> void encode(AddRagdollPacket<E> packet, FriendlyByteBuf buf) {
        buf.writeUtf(packet.typeId);
        buf.writeUUID(packet.uuid);
        buf.writeNbt(packet.extraData);

        buf.writeInt(packet.pieces.size());
        for (Map.Entry<E, Long> entry : packet.pieces.entrySet()) {
            buf.writeEnum(entry.getKey());
            buf.writeLong(entry.getValue());
        }
    }

    public static <E extends Enum<E>> AddRagdollPacket<E> decode(FriendlyByteBuf buf) {
        return RagdollRegistry.decode(buf);
    }

    public static <E extends Enum<E>> void handle(AddRagdollPacket<E> packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientRagdoll<E> ragdoll = new ClientRagdoll<>(packet.uuid, packet.typeId, packet.pieces, packet.extraData);
            ClientRagdollManager.register(ragdoll);
        });
        ctx.get().setPacketHandled(true);
    }
}