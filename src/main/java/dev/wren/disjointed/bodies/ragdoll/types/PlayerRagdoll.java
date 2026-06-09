package dev.wren.disjointed.bodies.ragdoll.types;

import dev.wren.disjointed.bodies.ragdoll.Ragdoll;
import dev.wren.disjointed.bodies.ragdoll.RagdollSlots;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import org.joml.Vector3d;
import org.valkyrienskies.core.api.bodies.ServerVsBody;
import org.valkyrienskies.core.api.world.PhysLevel;
import org.valkyrienskies.core.internal.joints.VSSphericalJoint;
import org.valkyrienskies.core.internal.world.VsiServerShipWorld;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.ValkyrienSkiesMod;
import org.valkyrienskies.mod.common.util.GameToPhysicsAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static dev.wren.disjointed.bodies.ragdoll.RagdollUtils.*;
import static dev.wren.disjointed.util.Utils.pxToBlocks;

public class PlayerRagdoll implements Ragdoll {

    private final HashMap<String, Long> pieces;
    private final UUID uuid;
    public final String username;

    public PlayerRagdoll() {
        this(null);
    }

    public PlayerRagdoll(String username) {
        this.pieces = new HashMap<>(6);
        this.uuid = UUID.randomUUID();
        this.username = username;
    }

    public PlayerRagdoll(UUID uuid, String username, HashMap<String, Long> pieces) {
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
    public void createJoints(ServerLevel level) {
        Long torso = getSlot(RagdollSlots.Player.TORSO);

        VSSphericalJoint torsoToHead = createRagdollJoint(torso, getSlot(RagdollSlots.Player.HEAD), new Vector3d(0, pxToBlocks(6), 0), new Vector3d(0, pxToBlocks(-4), 0));
        VSSphericalJoint torsoToLeftArm = createRagdollJoint(torso, getSlot(RagdollSlots.Player.LEFT_LEG), new Vector3d(pxToBlocks(4), pxToBlocks(6), 0), new Vector3d(pxToBlocks(-2), pxToBlocks(6), 0));
        VSSphericalJoint torsoToRightArm = createRagdollJoint(torso, getSlot(RagdollSlots.Player.RIGHT_ARM), new Vector3d(pxToBlocks(-4), pxToBlocks(6), 0), new Vector3d(pxToBlocks(2), pxToBlocks(6), 0));
        VSSphericalJoint torsoToLeftLeg = createRagdollJoint(torso, getSlot(RagdollSlots.Player.LEFT_LEG), new Vector3d(pxToBlocks(2), pxToBlocks(-6), 0), new Vector3d(0, pxToBlocks(6), 0));
        VSSphericalJoint torsoToRightLeg = createRagdollJoint(torso, getSlot(RagdollSlots.Player.RIGHT_LEG), new Vector3d(pxToBlocks(-2), pxToBlocks(-6), 0), new Vector3d(0, pxToBlocks(6), 0));

        GameToPhysicsAdapter gtpa = ValkyrienSkiesMod.getOrCreateGTPA(VSGameUtilsKt.getDimensionId(level));

        gtpa.addJoint(torsoToHead, 5, id -> {});
        gtpa.addJoint(torsoToLeftArm, 5, id -> {});
        gtpa.addJoint(torsoToRightArm, 5, id -> {});
        gtpa.addJoint(torsoToLeftLeg, 5, id -> {});
        gtpa.addJoint(torsoToRightLeg, 5, id -> {});
    }

    @Override
    public void changeCollision(PhysLevel level) {
        Long torso = getSlot(RagdollSlots.Player.TORSO);
        Long leftLeg = getSlot(RagdollSlots.Player.LEFT_LEG);
        Long rightLeg = getSlot(RagdollSlots.Player.RIGHT_LEG);

        level.disableCollisionBetween(torso, getSlot(RagdollSlots.Player.HEAD));
        level.disableCollisionBetween(torso, getSlot(RagdollSlots.Player.LEFT_ARM));
        level.disableCollisionBetween(torso, getSlot(RagdollSlots.Player.RIGHT_ARM));
        level.disableCollisionBetween(torso, leftLeg);
        level.disableCollisionBetween(torso, rightLeg);
        level.disableCollisionBetween(rightLeg, leftLeg);
    }

    @Override
    public void writeAdditionalData(CompoundTag tag) {
        tag.putString("username", username);
    }

    public static PlayerRagdoll create(ServerLevel level, Vector3d pos, String username, boolean isStatic) {
        PlayerRagdoll ragdoll = new PlayerRagdoll(username);
        VsiServerShipWorld shipWorld = VSGameUtilsKt.getShipObjectWorld(level);

        ragdoll.addSlot(RagdollSlots.Player.HEAD, shipWorld.createBody(createRagdollPieceData(level, headShape(), new Vector3d(pos).add(0, pxToBlocks(10), 0), 1000, isStatic)));
        ragdoll.addSlot(RagdollSlots.Player.TORSO, shipWorld.createBody(createRagdollPieceData(level, torsoShape(), new Vector3d(pos), 1000, isStatic)));

        ragdoll.addSlot(RagdollSlots.Player.LEFT_ARM, shipWorld.createBody(createRagdollPieceData(level, armOrLegShape(), new Vector3d(pos).add(pxToBlocks(6), 0, 0), 1000, isStatic)));
        ragdoll.addSlot(RagdollSlots.Player.RIGHT_ARM, shipWorld.createBody(createRagdollPieceData(level, armOrLegShape(), new Vector3d(pos).add(pxToBlocks(-6), 0, 0), 1000, isStatic)));

        ragdoll.addSlot(RagdollSlots.Player.LEFT_LEG, shipWorld.createBody(createRagdollPieceData(level, armOrLegShape(), new Vector3d(pos).add(pxToBlocks(2), pxToBlocks(-12), 0), 1000, isStatic)));
        ragdoll.addSlot(RagdollSlots.Player.RIGHT_LEG, shipWorld.createBody(createRagdollPieceData(level, armOrLegShape(), new Vector3d(pos).add(pxToBlocks(-2), pxToBlocks(-12), 0), 1000, isStatic)));

        return ragdoll;
    }
}
