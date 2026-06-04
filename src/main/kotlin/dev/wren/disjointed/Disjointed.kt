package dev.wren.disjointed

import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.LogManager

@Mod(MODID)
class Disjointed {

    constructor(context: FMLJavaModLoadingContext) {
        val modEventBus: IEventBus = context.modEventBus

        LOGGER.info("$NAME ($MODID) initialized!")
    }
}