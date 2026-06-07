package dev.wren.disjointed.bodies.ragdoll.client;

import java.util.HashMap;
import java.util.Map;

public class ClientRagdollGroupRegistry {
    private static final Map<String, ClientRagdollRenderer> REGISTRY = new HashMap<>();

    public static void register(String typeId, ClientRagdollRenderer renderer) {
        REGISTRY.put(typeId, renderer);
    }

    public static ClientRagdollRenderer get(String typeId) {
        return REGISTRY.getOrDefault(typeId, ClientRagdollRenderer.NONE);
    }
}