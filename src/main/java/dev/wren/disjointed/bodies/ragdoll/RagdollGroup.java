package dev.wren.disjointed.bodies.ragdoll;

import dev.wren.disjointed.util.Utils;
import org.valkyrienskies.core.api.bodies.ServerVsBody;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static dev.wren.disjointed.Disjointed.LOGGER;

public interface RagdollGroup  {

    Map<String, ServerVsBody> getPieces();
    List<String> getSlots();
    UUID getUUID();

    default void addSlot(String slot, ServerVsBody body) {
        if (!getSlots().contains(slot)) {
            LOGGER.error("Slot {} is not a valid slot for this ragdoll! Valid slots are: {}", slot, Utils.listAsString(getSlots()));
            return;
        }
        if (getPieces().containsKey(slot))
            LOGGER.warn("Slot {} already has a bound body!", slot);

        getPieces().put(slot, body);
    }

}
