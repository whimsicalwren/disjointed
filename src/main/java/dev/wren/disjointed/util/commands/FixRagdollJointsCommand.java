package dev.wren.disjointed.util.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import dev.wren.disjointed.bodies.ragdoll.RagdollManager;
import dev.wren.disjointed.bodies.ragdoll.Ragdoll;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class FixRagdollJointsCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> fixJoints() {
        return Commands.literal("fix_joints")
                .executes(ctx -> {
                    RagdollManager manager = RagdollManager.get(ctx.getSource().getLevel());

                    for (Ragdoll<?> group : manager.RAGDOLLS.values()) {
                        group.createJoints(ctx.getSource().getLevel());
                    }

                    return 1;
                });
    }

}
