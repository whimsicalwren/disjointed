package dev.wren.disjointed.util.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import dev.wren.disjointed.bodies.linked.LinkedBodyHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.valkyrienskies.core.api.bodies.ServerVsBody;

import static dev.wren.disjointed.Disjointed.LOGGER;

public class CreateBodyCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> create() {
        return Commands.literal("create_body")
                .then(Commands.argument("target", EntityArgument.entity())
                        .executes(ctx -> {
                            try {
                                Entity entity = EntityArgument.getEntity(ctx, "target");
                                ServerLevel level = ctx.getSource().getLevel();

                                ServerVsBody body = LinkedBodyHelper.createBody(level, entity);
                                long id = body.getId();

                                Component message = Component.literal("Created body with id " + id + " for entity " + entity.getName().getString());

                                ctx.getSource().sendSuccess(() -> message, false);
                                return 1;
                            } catch (Exception e) {
                                LOGGER.error(e.getMessage());
                                throw new RuntimeException(e);
                            }
                        }));
    }

}
