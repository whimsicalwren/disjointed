package dev.wren.disjointed.util.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;

public class DisjointedCommands {

    public static void register(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("disjointed")
                .then(CreateBodyCommand.create())
                .then(GetBodyCommand.get())
                .then(RemoveBodyCommand.remove())
                .then(CreateRagdollCommand.createClassic())
                .then(FixRagdollJointsCommand.fixJoints())
                ;

        event.getDispatcher().register(root);
    }

}
