package dev.wren.disjointed;

import dev.wren.disjointed.bodies.PhysTickHandler;
import dev.wren.disjointed.util.commands.DisjointedCommands;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.valkyrienskies.mod.common.ValkyrienSkiesMod;

@Mod(Disjointed.MODID)
public class Disjointed {

    public static final String MODID = "disjointed";
    public static final String NAME = "Disjointed";
    public static final Logger LOGGER = LogManager.getLogger("disjointed");

    public Disjointed(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        MinecraftForge.EVENT_BUS.addListener(DisjointedCommands::register);
        //ValkyrienSkiesMod.getApi().getPhysTickEvent().on(PhysTickHandler::onPhysTick);

        LOGGER.info("{} ({}) initialized!", NAME, MODID);
    }
}
