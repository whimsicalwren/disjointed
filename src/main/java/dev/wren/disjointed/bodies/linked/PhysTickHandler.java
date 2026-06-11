package dev.wren.disjointed.bodies.linked;

import org.valkyrienskies.core.api.bodies.ServerVsBody;
import org.valkyrienskies.core.api.events.PhysTickEvent;

import java.util.HashSet;
import java.util.Set;

import static dev.wren.disjointed.util.Utils.createBodyPose;

public class PhysTickHandler {

    public static final Set<EntityVsBodyExtension> LINKED_BODIES = new HashSet<>();

    public static void onPhysTick(PhysTickEvent event) {
        for (EntityVsBodyExtension extension : LINKED_BODIES) {
            ServerVsBody vody = extension.getBody();
            if (vody != null) {
                vody.setStatic(true);
                System.out.println("setting kinematic target to " + extension.getPosition() + " for body " + extension.getBodyId() + " for entity " + extension.getClass());
                vody.setKinematicTarget(createBodyPose(extension.getPosition()));
            }
        }
    }
}
