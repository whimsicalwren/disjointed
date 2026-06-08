package dev.wren.disjointed.bodies.ragdoll.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RagdollRenderers {

    public static void register() {
        ClientRagdollGroupRegistry.register("player", new PlayerRagdollRenderer());
    }

}
