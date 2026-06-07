package dev.wren.disjointed;

import dev.wren.disjointed.bodies.ragdoll.RagdollManager;
import dev.wren.disjointed.bodies.ragdoll.client.RagdollRenderers;
import dev.wren.disjointed.util.commands.DisjointedCommands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Disjointed.MODID)
public class Disjointed {

    public static final String MODID = "disjointed";
    public static final String NAME = "Disjointed";
    public static final Logger LOGGER = LogManager.getLogger("disjointed");

    public Disjointed(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        DisjointedNetwork.register();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> RagdollRenderers::register);

        MinecraftForge.EVENT_BUS.addListener(DisjointedCommands::register);
        MinecraftForge.EVENT_BUS.addListener(Disjointed::onPlayerJoin);
        MinecraftForge.EVENT_BUS.addListener(Disjointed::onServerStart);
        //ValkyrienSkiesMod.getApi().getPhysTickEvent().on(PhysTickHandler::onPhysTick);

        LOGGER.info("{} ({}) initialized!", NAME, MODID);
    }

    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        RagdollManager.syncAllRagdollsToPlayer(player);
    }

    public static void onServerStart(ServerStartedEvent event) {
        MinecraftServer server = event.getServer();
        for (ServerLevel level : server.getAllLevels()) {
            RagdollManager.get(level);
        }
    }

}
