package dev.wren.disjointed.bodies.ragdoll;

import org.valkyrienskies.core.api.bodies.ServerVsBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerRagdollGroup implements RagdollGroup {

    public static final String HEAD_SLOT = "HEAD";
    public static final String TORSO_SLOT = "TORSO";
    public static final String LEFT_ARM_SLOT = "ARM_LEFT";
    public static final String RIGHT_ARM_SLOT = "ARM_RIGHT";
    public static final String LEFT_LEG_SLOT = "LEG_LEFT";
    public static final String RIGHT_LEG_SLOT = "LEG_RIGHT";

    private final HashMap<String, ServerVsBody> pieces;
    private final UUID uuid;

    public PlayerRagdollGroup() {
        this.pieces = new HashMap<>();
        this.uuid = UUID.randomUUID();
    }

    @Override
    public Map<String, ServerVsBody> getPieces() {
        return pieces;
    }

    @Override
    public List<String> getSlots() {
        return List.of(HEAD_SLOT, TORSO_SLOT, LEFT_ARM_SLOT, RIGHT_ARM_SLOT, LEFT_LEG_SLOT, RIGHT_LEG_SLOT);
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }


}
