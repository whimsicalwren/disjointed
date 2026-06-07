package dev.wren.disjointed.bodies.ragdoll.packet;

import dev.wren.disjointed.bodies.ragdoll.client.ClientRagdollManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClearAllRagdollsPacket {

    public ClearAllRagdollsPacket() {}

    public static void encode(ClearAllRagdollsPacket packet, FriendlyByteBuf buf) {}

    public static ClearAllRagdollsPacket decode(FriendlyByteBuf buf) {
        return new ClearAllRagdollsPacket();
    }

    public static void handle(ClearAllRagdollsPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(ClientRagdollManager::clearAll);
        ctx.get().setPacketHandled(true);
    }
}
