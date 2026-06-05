package dev.wren.disjointed.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;

public class DisjointedCommands {

    public static void register(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("disjointed")
                .then(CreateBodyCommand.create())
                .then(RemoveBodyCommand.remove())
                ;

        event.getDispatcher().register(root);
    }

}
