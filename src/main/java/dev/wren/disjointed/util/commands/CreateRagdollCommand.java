package dev.wren.disjointed.util.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import dev.wren.disjointed.bodies.ragdoll.PlayerRagdollGroup;
import dev.wren.disjointed.bodies.ragdoll.PlayerRagdollHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import org.joml.Vector3d;

public class CreateRagdollCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> createClassic() {
        return Commands.literal("create_ragdoll_classic")
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .executes(ctx -> {
                            BlockPos pos = BlockPosArgument.getBlockPos(ctx, "pos");

                            Vector3d posVec = new Vector3d(pos.getX(), pos.getY(), pos.getZ());

                            PlayerRagdollGroup ragdoll = PlayerRagdollHelper.createRagdollClassic(ctx.getSource().getLevel(), posVec);

                            Component message = Component.literal("Created ragdoll " + ragdoll.getUUID());

                            ctx.getSource().sendSuccess(() -> message, false);

                            return 1;
                        }));
    }

    public static ArgumentBuilder<CommandSourceStack, ?> createSlim() {
        return Commands.literal("create_ragdoll_slim")
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .executes(ctx -> {
                            BlockPos pos = BlockPosArgument.getBlockPos(ctx, "pos");

                            Vector3d posVec = new Vector3d(pos.getX(), pos.getY(), pos.getZ());

                            PlayerRagdollGroup ragdoll = PlayerRagdollHelper.createRagdollSlim(ctx.getSource().getLevel(), posVec);

                            Component message = Component.literal("Created ragdoll " + ragdoll.getUUID());

                            ctx.getSource().sendSuccess(() -> message, false);

                            return 1;
                        }));
    }

}
