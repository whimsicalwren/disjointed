package dev.wren.disjointed.bodies.ragdoll.group;

import dev.wren.disjointed.bodies.ragdoll.RagdollSlots;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import org.valkyrienskies.core.api.bodies.ServerVsBody;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerRagdollGroup implements RagdollGroup {

    private final HashMap<String, Long> pieces;
    private final UUID uuid;
    public final String username;

    public PlayerRagdollGroup() {
        this(null);
    }

    public PlayerRagdollGroup(String username) {
        this.pieces = new HashMap<>(6);
        this.uuid = UUID.randomUUID();
        this.username = username;
    }

    public PlayerRagdollGroup(UUID uuid, String username, HashMap<String, Long> pieces) {
        this.pieces = pieces;
        this.uuid = uuid;
        this.username = username;
    }

    @Override
    public Map<String, Long> getPieces() {
        return pieces;
    }

    @Override
    public List<String> getSlots() {
        return RagdollSlots.Player.allSlots();
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public String getTypeId() {
        return "player";
    }

    @Override
    public Long getSlot(String slot) {
        return pieces.get(slot);
    }

    @Override
    public void remove(ServerLevel level) {
        for (Long id : this.pieces.values()) {
            ServerVsBody body = (ServerVsBody) VSGameUtilsKt.getAllBodies(level).getById(id);
            if (body != null) VSGameUtilsKt.getShipObjectWorld(level).deleteBody(body);
        }
    }

    @Override
    public void writeAdditionalData(CompoundTag tag) {
        tag.putString("username", username);
    }
}
