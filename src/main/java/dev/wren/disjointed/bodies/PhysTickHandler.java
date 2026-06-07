package dev.wren.disjointed.bodies;

import org.valkyrienskies.core.api.bodies.ServerVsBody;
import org.valkyrienskies.core.api.events.PhysTickEvent;

import java.util.HashSet;
import java.util.Set;

public class PhysTickHandler {

    public static final Set<EntityVsBodyExtension> LINKED_BODIES = new HashSet<>();

    public static void onPhysTick(PhysTickEvent event) {
        for (EntityVsBodyExtension extension : LINKED_BODIES) {
            ServerVsBody vody = extension.getBody();
            if (vody != null) {
                vody.setPosition(extension.getPosition());
                //vody.setKinematicTarget(createBodyPose(entity.position(), entity.getBoundingBox()));
            }
        }
    }

}
