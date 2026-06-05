package dev.wren.disjointed.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import dev.wren.disjointed.bodies.PlayerBodyHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.valkyrienskies.core.api.bodies.ServerVsBody;

public class CreateBodyCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> create() {
        return Commands.literal("create")
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    ServerLevel level = ctx.getSource().getLevel();

                    ServerVsBody body = PlayerBodyHelper.createBody(level, player);
                    long id = body.getId();

                    Component message = Component.literal("Created body with id " + id + " for player " + player.getName().getString());

                    ctx.getSource().sendSuccess(() -> message, false);

                    return 1;
                });
    }

}
