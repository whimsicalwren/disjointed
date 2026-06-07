package dev.wren.disjointed.util.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import dev.wren.disjointed.bodies.EntityVsBodyExtension;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

public class GetBodyCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> get() {
        return Commands.literal("get_body")
                .then(Commands.argument("target", EntityArgument.entity())
                        .executes(ctx -> {
                            Entity entity = EntityArgument.getEntity(ctx, "target");
                            EntityVsBodyExtension ext = (EntityVsBodyExtension) entity;

                            Component message = ext.hasBody() ?
                                    Component.literal("Found body with id " + ext.getBodyId() + " for entity " + entity.getName().getString())
                                    :
                                    Component.literal("No body found for entity " + entity.getName().getString());

                            ctx.getSource().sendSuccess(() -> message, false);
                            return 1;
                        }));
    }

}
