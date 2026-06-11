package dev.wren.disjointed.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.wren.disjointed.bodies.ragdoll.client.ModelPartExtension;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ModelPart.class)
public abstract class ModelPartMixin implements ModelPartExtension {

    @Invoker("compile")
    public abstract void invokeCompile(PoseStack.Pose pose, VertexConsumer consumer, int light, int overlay, float r, float g, float b, float a);

    @Unique
    @Override
    public void renderWithoutChildren(PoseStack poseStack, VertexConsumer consumer, int light, int overlay, float r, float g, float b, float a) {
        ModelPart self = (ModelPart) (Object) this;
        if (self.visible && (!self.skipDraw)) {
            poseStack.pushPose();
            self.translateAndRotate(poseStack);
            invokeCompile(poseStack.last(), consumer, light, overlay, r, g, b, a);
            poseStack.popPose();
        }
    }
}
