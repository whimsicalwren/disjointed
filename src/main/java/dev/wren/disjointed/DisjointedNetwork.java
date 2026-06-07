package dev.wren.disjointed;

import dev.wren.disjointed.bodies.ragdoll.packet.AddRagdollPacket;
import dev.wren.disjointed.bodies.ragdoll.packet.ClearAllRagdollsPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class DisjointedNetwork {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(Disjointed.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int id = 0;

    public static void register() {
        CHANNEL.registerMessage(id++,
                AddRagdollPacket.class,
                AddRagdollPacket::encode,
                AddRagdollPacket::decode,
                AddRagdollPacket::handle
        );
        CHANNEL.registerMessage(id++,
                ClearAllRagdollsPacket.class,
                ClearAllRagdollsPacket::encode,
                ClearAllRagdollsPacket::decode,
                ClearAllRagdollsPacket::handle
        );
    }

    public static void sendToAll(Object packet) {
        System.out.println("send to all");
        CHANNEL.send(PacketDistributor.ALL.noArg(), packet);
    }

    public static void sendToPlayer(ServerPlayer player, Object packet) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    public static void sendToServer(Object packet) {
        CHANNEL.sendToServer(packet);
    }
}