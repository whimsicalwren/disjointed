package dev.wren.disjointed.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.valkyrienskies.core.api.bodies.ServerVsBody;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

public class RemoveBodyCommand {


    public static ArgumentBuilder<CommandSourceStack, ?> remove() {
        return Commands.literal("remove")
                .then(Commands.argument("id", IntegerArgumentType.integer())
                        .executes(ctx -> {
                            int id = IntegerArgumentType.getInteger(ctx, "id");
                            ServerVsBody body = (ServerVsBody) VSGameUtilsKt.getAllBodies(ctx.getSource().getLevel()).getById(id);

                            if (body != null) {
                                VSGameUtilsKt.getShipObjectWorld(ctx.getSource().getLevel()).deleteBody(body);
                                Component message = Component.literal("Removed body with id " + id);
                                ctx.getSource().sendSuccess(() -> message, false);
                            }

                            return 1;
                        }));
    }
}
