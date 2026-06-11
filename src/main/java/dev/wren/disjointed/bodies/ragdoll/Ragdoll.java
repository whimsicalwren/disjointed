package dev.wren.disjointed.bodies.ragdoll;

import dev.wren.disjointed.util.Utils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import org.valkyrienskies.core.api.bodies.ServerVsBody;
import org.valkyrienskies.core.api.world.PhysLevel;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static dev.wren.disjointed.Disjointed.LOGGER;

public interface Ragdoll {

    Map<String, Long> getPieces();
    List<String> getSlots();
    UUID getUUID();
    String getTypeId();

    Long getSlot(String slot);

    void createJoints(ServerLevel level);
    void changeCollision(PhysLevel level);

    default void remove(ServerLevel level) {
        for (Long id : getPieces().values()) {
            ServerVsBody body = (ServerVsBody) VSGameUtilsKt.getAllBodies(level).getById(id);
            if (body != null) VSGameUtilsKt.getShipObjectWorld(level).deleteBody(body);
        }
    }


    default ServerVsBody addSlot(String slot, ServerVsBody body) {
        if (!getSlots().contains(slot)) {
            LOGGER.error("Slot {} is not a valid slot for this ragdoll! Valid slots are: {}", slot, Utils.listAsString(getSlots()));
            return null;
        }
        if (getPieces().containsKey(slot))
            LOGGER.warn("Slot {} already has a bound body!", slot);

        getPieces().put(slot, body.getId());

        return body;
    }

    default CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", getTypeId());
        tag.putUUID("uuid", getUUID());
        tag.put("pieces", Utils.writeMap(getPieces(), CompoundTag::putLong));

        writeAdditionalData(tag);
        return tag;
    }

    default void writeAdditionalData(CompoundTag nbt) {}

    default CompoundTag getAdditionalData() {
        CompoundTag tag = new CompoundTag();
        writeAdditionalData(tag);
        return tag;
    }

}
