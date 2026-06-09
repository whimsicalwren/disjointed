package dev.wren.disjointed.util.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import dev.wren.disjointed.bodies.linked.EntityVsBodyExtension;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.valkyrienskies.core.api.bodies.ServerVsBody;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

public class RemoveBodyCommand {


    public static ArgumentBuilder<CommandSourceStack, ?> remove() {
        return Commands.literal("remove_body")
                .then(Commands.argument("target", EntityArgument.entity())
                        .executes(ctx -> {
                            Entity entity = EntityArgument.getEntity(ctx, "target");
                            EntityVsBodyExtension ext = (EntityVsBodyExtension) entity;
                            Component message;

                            if (ext.hasBody()) {
                                ServerVsBody vody = ext.getBody();
                                message = Component.literal("Successfully removed body with id " + vody.getId() + " from entity " + entity.getName().getString());
                                VSGameUtilsKt.getShipObjectWorld(ctx.getSource().getLevel()).deleteBody(vody);
                                ext.removeBody();
                            } else {
                                message = Component.literal("There is no body linked to entity " + entity.getName().getString());
                            }

                            ctx.getSource().sendSuccess(() -> message, false);
                            return 1;
                        }));
    }
}
