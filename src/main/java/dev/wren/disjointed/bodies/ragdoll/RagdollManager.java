package dev.wren.disjointed.bodies.ragdoll;

import dev.wren.disjointed.Disjointed;
import dev.wren.disjointed.DisjointedNetwork;
import dev.wren.disjointed.bodies.ragdoll.group.Ragdoll;
import dev.wren.disjointed.bodies.ragdoll.packet.AddRagdollPacket;
import dev.wren.disjointed.bodies.ragdoll.packet.ClearAllRagdollsPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RagdollManager extends SavedData {
    private static final String NAME = "disjointed_ragdolls";

    public final Map<UUID, Ragdoll> RAGDOLLS = new HashMap<>();

    public static RagdollManager get(ServerLevel level) {
        DimensionDataStorage storage = level.getDataStorage();
        return storage.computeIfAbsent(RagdollManager::load, RagdollManager::new, NAME);
    }

    public static RagdollManager load(CompoundTag tag) {
        RagdollManager data = new RagdollManager();

        ListTag list = tag.getList("ragdolls", Tag.TAG_COMPOUND);
        for (Tag t : list) {
            CompoundTag ragdollTag = (CompoundTag) t;

            Ragdoll group = RagdollRegistry.deserialize(ragdollTag);

            data.RAGDOLLS.put(group.getUUID(), group);
        }

        Disjointed.LOGGER.info("Loaded {} ragdolls from saved data.", data.RAGDOLLS.size());
        return data;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, Ragdoll> entry : RAGDOLLS.entrySet()) {
            list.add(entry.getValue().serialize());
        }
        tag.put("ragdolls", list);
        Disjointed.LOGGER.info("Saved {} ragdolls to data.", tag.size());
        return tag;
    }

    public void addRagdoll(Ragdoll ragdoll) {
        RAGDOLLS.put(ragdoll.getUUID(), ragdoll);

        DisjointedNetwork.sendToAll(new AddRagdollPacket(ragdoll.getUUID(), ragdoll.getTypeId(), ragdoll.getPieces(), ragdoll.getAdditionalData()));

        setDirty();
    }

    public static void syncAllRagdollsToPlayer(ServerPlayer player) {
        RagdollManager manager = RagdollManager.get(player.serverLevel());

        Disjointed.LOGGER.info("Syncing all ragdolls to player {} ({})", player.getName().getString(), player.getUUID());

        DisjointedNetwork.sendToPlayer(player, new ClearAllRagdollsPacket());

        for (Ragdoll ragdoll : manager.RAGDOLLS.values()) {
            DisjointedNetwork.sendToPlayer(player, new AddRagdollPacket(ragdoll.getUUID(), ragdoll.getTypeId(), ragdoll.getPieces(), ragdoll.getAdditionalData()));
        }

    }
}
