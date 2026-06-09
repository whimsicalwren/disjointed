package dev.wren.disjointed.util.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.wren.disjointed.bodies.ragdoll.RagdollFactory;
import dev.wren.disjointed.bodies.ragdoll.RagdollRegistry;
import dev.wren.disjointed.bodies.ragdoll.group.RagdollHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

import java.util.List;

public class CreateRagdollCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> create() {
        return Commands.literal("create_ragdoll")
                        .then(Commands.argument("type", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    RagdollRegistry.getTypes().forEach(builder::suggest);
                                    return builder.buildFuture();
                                })
                                .then(Commands.argument("static", BoolArgumentType.bool())
                                        .executes(ctx -> createRagdoll(ctx, ""))
                                .then(Commands.argument("args", StringArgumentType.greedyString())
                                        .executes(ctx -> createRagdoll(ctx, StringArgumentType.getString(ctx, "args"))))
                        ));
    }

    private static int createRagdoll(CommandContext<CommandSourceStack> ctx, String argsString) {
        ServerLevel level = ctx.getSource().getLevel();
        Vec3 pos = ctx.getSource().getPosition();
        String type = StringArgumentType.getString(ctx, "type");
        boolean isStatic = BoolArgumentType.getBool(ctx, "static");

        if (!RagdollRegistry.isValidType(type)) {
            ctx.getSource().sendFailure(Component.literal("Type " + type + " is not a registered ragdoll type!"));
            return 0; // this shouldn't happen since it pulls suggestions from registry but i'm checking anyways
        }

        return RagdollHelper.createRagdoll(level, pos, type, argsString, isStatic);
    }
}
