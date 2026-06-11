package dev.wren.disjointed.bodies.ragdoll;

import java.util.Arrays;
import java.util.List;

public class RagdollSlots {
        public enum NONE {}


        public enum Humanoid {
                HEAD,
                TORSO,
                LEFT_ARM,
                RIGHT_ARM,
                LEFT_LEG,
                RIGHT_LEG
                ;

                public static List<Humanoid> asList() {
                        return Arrays.stream(values()).toList();
                }
        }

        public enum EnderDragon {
                JAW,
                LIP,
                HEAD,
                NECK_1, NECK_2, NECK_3, NECK_4, NECK_5,
                BODY,
                LEFT_WING, LEFT_WING_TIP,
                RIGHT_WING, RIGHT_WING_TIP,
                LEFT_FRONT_LEG, LEFT_FRONT_LEG_TIP, LEFT_FRONT_FOOT,
                LEFT_REAR_LEG, LEFT_REAR_LEG_TIP, LEFT_REAR_FOOT,
                RIGHT_FRONT_LEG, RIGHT_FRONT_LEG_TIP, RIGHT_FRONT_FOOT,
                RIGHT_REAR_LEG, RIGHT_REAR_LEG_TIP, RIGHT_REAR_FOOT,
                TAIL_1, TAIL_2, TAIL_3, TAIL_4, TAIL_5, TAIL_6,
                TAIL_7, TAIL_8, TAIL_9, TAIL_10, TAIL_11, TAIL_12
                ;

                public static List<EnderDragon> asList() {
                        return Arrays.stream(values()).toList();
                }
        }
}
