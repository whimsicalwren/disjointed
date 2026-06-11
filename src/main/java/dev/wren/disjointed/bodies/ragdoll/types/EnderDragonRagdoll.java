package dev.wren.disjointed.bodies.ragdoll.types;

import dev.wren.disjointed.bodies.ragdoll.Ragdoll;
import dev.wren.disjointed.bodies.ragdoll.RagdollSlots;
import net.minecraft.server.level.ServerLevel;
import org.joml.Vector3d;
import org.valkyrienskies.core.api.world.PhysLevel;
import org.valkyrienskies.core.internal.world.VsiServerShipWorld;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.util.*;

import static dev.wren.disjointed.bodies.ragdoll.RagdollShapes.EnderDragon.*;
import static dev.wren.disjointed.bodies.ragdoll.RagdollSlots.EnderDragon.*;
import static dev.wren.disjointed.bodies.ragdoll.RagdollUtils.createRagdollJoint;
import static dev.wren.disjointed.bodies.ragdoll.RagdollUtils.createRagdollPieceData;
import static dev.wren.disjointed.util.Utils.pxToBlocks;

public class EnderDragonRagdoll implements Ragdoll<RagdollSlots.EnderDragon> {

    private final EnumMap<RagdollSlots.EnderDragon, Long> pieces;
    private final UUID uuid;


    public EnderDragonRagdoll() {
        this.pieces = new EnumMap<>(RagdollSlots.EnderDragon.class);
        this.uuid = UUID.randomUUID();
    }

    public EnderDragonRagdoll(UUID uuid, EnumMap<RagdollSlots.EnderDragon, Long> pieces) {
        this.pieces = pieces;
        this.uuid = uuid;
    }

    @Override
    public EnumMap<RagdollSlots.EnderDragon, Long> getPieces() {
        return pieces;
    }

    @Override
    public List<RagdollSlots.EnderDragon> getSlots() {
        return RagdollSlots.EnderDragon.asList();
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public String getTypeId() {
        return "ender_dragon";
    }

    @Override
    public Long getSlot(RagdollSlots.EnderDragon slot) {
        return pieces.get(slot);
    }

    @Override
    public void createJoints(ServerLevel level) {

    }

    @Override
    public void changeCollision(PhysLevel level) {

    }

    public static EnderDragonRagdoll create(ServerLevel level, Vector3d pos, boolean isStatic) {
        EnderDragonRagdoll ragdoll = new EnderDragonRagdoll();
        VsiServerShipWorld shipWorld = VSGameUtilsKt.getShipObjectWorld(level);

        ragdoll.addSlot(JAW, shipWorld.createBody(createRagdollPieceData(level, lowerMouth(), new Vector3d(pos), 1000, isStatic)));
        ragdoll.addSlot(LIP, shipWorld.createBody(createRagdollPieceData(level, upperMouth(), new Vector3d(pos), 1000, isStatic)));

        ragdoll.addSlot(HEAD, shipWorld.createBody(createRagdollPieceData(level, head(), new Vector3d(pos), 1000, isStatic)));
        ragdoll.addSlot(BODY, shipWorld.createBody(createRagdollPieceData(level, body(), new Vector3d(pos), 1000, isStatic)));

        ragdoll.addSlot(LEFT_WING, shipWorld.createBody(createRagdollPieceData(level, wing(), new Vector3d(pos), 1000, isStatic)));
        ragdoll.addSlot(LEFT_WING_TIP, shipWorld.createBody(createRagdollPieceData(level, wingTip(), new Vector3d(pos), 1000, isStatic)));

        ragdoll.addSlot(RIGHT_WING, shipWorld.createBody(createRagdollPieceData(level, wing(), new Vector3d(pos), 1000, isStatic)));
        ragdoll.addSlot(RIGHT_WING_TIP, shipWorld.createBody(createRagdollPieceData(level, wingTip(), new Vector3d(pos), 1000, isStatic)));

        ragdoll.addSlot(LEFT_FRONT_LEG, shipWorld.createBody(createRagdollPieceData(level, frontLeg(), new Vector3d(pos), 1000, isStatic)));
        ragdoll.addSlot(LEFT_FRONT_LEG_TIP, shipWorld.createBody(createRagdollPieceData(level, frontLegTip(), new Vector3d(pos), 1000, isStatic)));
        ragdoll.addSlot(LEFT_FRONT_FOOT, shipWorld.createBody(createRagdollPieceData(level, frontFoot(), new Vector3d(pos), 1000, isStatic)));

        ragdoll.addSlot(RIGHT_FRONT_LEG, shipWorld.createBody(createRagdollPieceData(level, frontLeg(), new Vector3d(pos), 1000, isStatic)));
        ragdoll.addSlot(RIGHT_FRONT_LEG_TIP, shipWorld.createBody(createRagdollPieceData(level, frontLegTip(), new Vector3d(pos), 1000, isStatic)));
        ragdoll.addSlot(RIGHT_FRONT_FOOT, shipWorld.createBody(createRagdollPieceData(level, frontFoot(), new Vector3d(pos), 1000, isStatic)));

        ragdoll.addSlot(LEFT_REAR_LEG, shipWorld.createBody(createRagdollPieceData(level, rearLeg(), new Vector3d(pos), 1000, isStatic)));
        ragdoll.addSlot(LEFT_REAR_LEG_TIP, shipWorld.createBody(createRagdollPieceData(level, rearLegTip(), new Vector3d(pos), 1000, isStatic)));
        ragdoll.addSlot(LEFT_REAR_FOOT, shipWorld.createBody(createRagdollPieceData(level, rearFoot(), new Vector3d(pos), 1000, isStatic)));

        ragdoll.addSlot(RIGHT_REAR_LEG, shipWorld.createBody(createRagdollPieceData(level, rearLeg(), new Vector3d(pos), 1000, isStatic)));
        ragdoll.addSlot(RIGHT_REAR_LEG_TIP, shipWorld.createBody(createRagdollPieceData(level, rearLegTip(), new Vector3d(pos), 1000, isStatic)));
        ragdoll.addSlot(RIGHT_REAR_FOOT, shipWorld.createBody(createRagdollPieceData(level, rearFoot(), new Vector3d(pos), 1000, isStatic)));

        return ragdoll;
    }
}
