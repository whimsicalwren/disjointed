package dev.wren.disjointed.bodies.ragdoll;

import java.util.List;

public class RagdollSlots {
    public static class Player {
        public static final String HEAD = "HEAD";
        public static final String TORSO = "TORSO";
        public static final String LEFT_ARM = "ARM_LEFT";
        public static final String RIGHT_ARM = "ARM_RIGHT";
        public static final String LEFT_LEG = "LEG_LEFT";
        public static final String RIGHT_LEG = "LEG_RIGHT";

        public static List<String> allSlots() {
            return List.of(HEAD, TORSO, LEFT_ARM, RIGHT_ARM, LEFT_LEG, RIGHT_LEG);
        }
    }
}
