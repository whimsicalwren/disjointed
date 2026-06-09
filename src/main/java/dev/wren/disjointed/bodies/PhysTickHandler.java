package dev.wren.disjointed.bodies;

import dev.wren.disjointed.bodies.ragdoll.RagdollManager;
import dev.wren.disjointed.bodies.ragdoll.group.Ragdoll;
import net.minecraft.server.level.ServerLevel;
import org.valkyrienskies.core.api.bodies.ServerVsBody;
import org.valkyrienskies.core.api.events.PhysTickEvent;
import org.valkyrienskies.core.api.world.PhysLevel;

import java.util.HashSet;
import java.util.Set;

import static dev.wren.disjointed.util.Utils.createBodyPose;

public class PhysTickHandler {

    public static final Set<EntityVsBodyExtension> LINKED_BODIES = new HashSet<>();

    public static void onPhysTick(PhysTickEvent event) {
        tickLinkedBodies();
    }

    public static void tickLinkedBodies() {
        for (EntityVsBodyExtension extension : LINKED_BODIES) {
            ServerVsBody vody = extension.getBody();
            if (vody != null) {
                vody.setKinematicTarget(createBodyPose(extension.getPosition()));
            }
        }
    }

}
