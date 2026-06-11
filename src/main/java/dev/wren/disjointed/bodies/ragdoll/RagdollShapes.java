package dev.wren.disjointed.bodies.ragdoll;

import org.joml.Vector3d;
import org.valkyrienskies.core.api.bodies.shape.BodyShapeData;
import org.valkyrienskies.core.api.bodies.shape.BoxBodyShapeData;

import static dev.wren.disjointed.util.Utils.pxToBlocks;

public class RagdollShapes {

    public static class Humanoid {
        public static BodyShapeData head() {
            return new BoxBodyShapeData(new Vector3d(0.5));
        }

        public static BodyShapeData torso() {
            return new BoxBodyShapeData(new Vector3d(0.5, 0.75, 0.25));
        }

        public static BodyShapeData armOrLeg() {
            return new BoxBodyShapeData(new Vector3d(0.25, 0.75, 0.25));
        }

        public static BodyShapeData armSlim() {
            return new BoxBodyShapeData(new Vector3d(0.1875, 0.75, 0.25));
        }
    }

    public static class EnderDragon {
        public static BodyShapeData head() {
            return shapeDataPx(16, 16, 16);
        }

        public static BodyShapeData upperMouth() {
            return shapeDataPx(12, 5, 16);
        }

        public static BodyShapeData lowerMouth() {
            return shapeDataPx(12, 4, 16);
        }

        public static BodyShapeData body() {
            return shapeDataPx(24, 24, 64);
        }

        public static BodyShapeData wing() {
            return shapeDataPx(56, 8, 8);
        }

        public static BodyShapeData wingTip() {
            return shapeDataPx(56, 4, 4);
        }

        public static BodyShapeData frontLeg() {
            return shapeDataPx(8, 24, 8);
        }

        public static BodyShapeData frontLegTip() {
            return shapeDataPx(6, 24, 6);
        }

        public static BodyShapeData frontFoot() {
            return shapeDataPx(8, 4, 16);
        }

        public static BodyShapeData rearLeg() {
            return shapeDataPx(16, 32, 16);
        }

        public static BodyShapeData rearLegTip() {
            return shapeDataPx(12, 32, 12);
        }

        public static BodyShapeData rearFoot() {
            return shapeDataPx(18, 6, 24);
        }

        public static BodyShapeData neckOrTail() {
            return shapeDataPx(10, 10, 10);
        }
    }

    public static BodyShapeData shapeData(double d) {
        return new BoxBodyShapeData(new Vector3d(d));
    }

    public static BodyShapeData shapeData(double x, double y, double z) {
        return new BoxBodyShapeData(new Vector3d(x, y, z));
    }

    public static BodyShapeData shapeDataPx(double d) {
        return new BoxBodyShapeData(new Vector3d(pxToBlocks(d)));
    }

    public static BodyShapeData shapeDataPx(double x, double y, double z) {
        return new BoxBodyShapeData(new Vector3d(pxToBlocks(x), pxToBlocks(y), pxToBlocks(z)));
    }

    public static BodyShapeData ph() {
        return new BoxBodyShapeData(new Vector3d(1));
    }

}
