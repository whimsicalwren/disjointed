package dev.wren.disjointed.bodies.ragdoll.packet;

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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class AddRagdollPacket {
    private final UUID uuid;
    private final String typeId;
    private final Map<String, Long> pieces;
    private final CompoundTag extraData;

    public AddRagdollPacket(UUID uuid, String typeId, Map<String, Long> pieces, CompoundTag extraData) {
        this.uuid = uuid;
        this.typeId = typeId;
        this.pieces = pieces;
        this.extraData = extraData;
    }

    public static void encode(AddRagdollPacket packet, FriendlyByteBuf buf) {
        buf.writeUUID(packet.uuid);
        buf.writeUtf(packet.typeId);
        buf.writeNbt(packet.extraData);

        buf.writeInt(packet.pieces.size());
        for (Map.Entry<String, Long> entry : packet.pieces.entrySet()) {
            buf.writeUtf(entry.getKey());
            buf.writeLong(entry.getValue());
        }
    }

    public static AddRagdollPacket decode(FriendlyByteBuf buf) {
        UUID uuid = buf.readUUID();
        String typeId = buf.readUtf();
        CompoundTag extraData = buf.readNbt();

        int size = buf.readInt();
        Map<String, Long> pieces = new HashMap<>();
        for (int i = 0; i < size; i++) {
            pieces.put(buf.readUtf(), buf.readLong());
        }

        return new AddRagdollPacket(uuid, typeId, pieces, extraData);
    }

    public static void handle(AddRagdollPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientRagdoll ragdoll = new ClientRagdoll(packet.uuid, packet.typeId, packet.pieces, packet.extraData);
            ClientRagdollManager.register(ragdoll);
        });
        ctx.get().setPacketHandled(true);
    }
}