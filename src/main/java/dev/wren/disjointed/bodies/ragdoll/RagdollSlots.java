package dev.wren.disjointed.bodies.ragdoll;

import java.util.List;

public class RagdollSlots {
    public static class Humanoid {
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

    public static class EnderDragon {
        public static final String LOWER_MOUTH = "MOUTH_LOWER";
        public static final String UPPER_MOUTH = "MOUTH_UPPER";
        public static final String HEAD = "HEAD";
        public static String NECK(int segment) { // 1 - 5
            return "NECK_" + segment;
        }
        public static final String BODY = "BODY";
        public static final String LEFT_WING = "LEFT_WING";
        public static final String LEFT_WING_TIP = "LEFT_WING_TIP";
        public static final String RIGHT_WING = "RIGHT_WING";
        public static final String RIGHT_WING_TIP = "RIGHT_WING_TIP";

        public static final String LEFT_FRONT_LEG = "LEFT_FRONT_LEG";
        public static final String LEFT_FRONT_LEG_TIP = "LEFT_FRONT_LEG_TIP";
        public static final String LEFT_FRONT_FOOT = "LEFT_FRONT_FOOT";
        public static final String LEFT_REAR_LEG = "LEFT_REAR_LEG";
        public static final String LEFT_REAR_LEG_TIP = "LEFT_REAR_LEG_TIP";
        public static final String LEFT_REAR_FOOT = "LEFT_REAR_FOOT";

        public static final String RIGHT_FRONT_LEG = "RIGHT_FRONT_LEG";
        public static final String RIGHT_FRONT_LEG_TIP = "RIGHT_FRONT_LEG_TIP";
        public static final String RIGHT_FRONT_FOOT = "RIGHT_FRONT_FOOT";
        public static final String RIGHT_REAR_LEG = "RIGHT_REAR_LEG";
        public static final String RIGHT_REAR_LEG_TIP = "RIGHT_REAR_LEG_TIP";
        public static final String RIGHT_REAR_FOOT = "RIGHT_REAR_FOOT";

        public static String TAIL(int segment) { // 1 - 12
            return "TAIL_" + segment;
        }

        public static List<String> allSlots() {
            return List.of(LOWER_MOUTH, UPPER_MOUTH, HEAD, NECK(1), NECK(2), NECK(3),
                    NECK(4), NECK(5), BODY, LEFT_FRONT_LEG, LEFT_FRONT_LEG_TIP, LEFT_FRONT_FOOT,
                    LEFT_REAR_LEG, LEFT_REAR_LEG_TIP, LEFT_REAR_FOOT, RIGHT_FRONT_LEG, RIGHT_FRONT_LEG_TIP,
                    RIGHT_FRONT_FOOT, RIGHT_REAR_LEG, RIGHT_REAR_LEG_TIP, RIGHT_REAR_FOOT,
                    LEFT_WING, LEFT_WING_TIP, RIGHT_WING, RIGHT_WING_TIP, TAIL(1), TAIL(2), TAIL(3),
                    TAIL(4), TAIL(5), TAIL(6), TAIL(7), TAIL(8), TAIL(9),
                    TAIL(10), TAIL(11), TAIL(12));
        }
    }

    public enum Quadripedal {
        LEFT_FRONT,
        RIGHT_FRONT,
        LEFT_REAR,
        RIGHT_REAR
    }
}
