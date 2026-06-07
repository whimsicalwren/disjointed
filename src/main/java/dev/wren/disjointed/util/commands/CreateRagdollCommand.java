package dev.wren.disjointed.util.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import dev.wren.disjointed.bodies.ragdoll.group.PlayerRagdollGroup;
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
                        .then(Commands.argument("username", StringArgumentType.greedyString())
                        .executes(ctx -> {
                            BlockPos pos = BlockPosArgument.getBlockPos(ctx, "pos");
                            String username = StringArgumentType.getString(ctx, "username");

                            Vector3d posVec = new Vector3d(pos.getX(), pos.getY(), pos.getZ());

                            PlayerRagdollGroup ragdoll = PlayerRagdollHelper.createPlayerRagdoll(ctx.getSource().getPlayerOrException(), posVec, username);

                            Component message = Component.literal("Created ragdoll " + ragdoll.getUUID());

                            ctx.getSource().sendSuccess(() -> message, false);

                            return 1;
                        })));
    }

}
