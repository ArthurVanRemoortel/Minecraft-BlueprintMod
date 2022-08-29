package com.arthur.JavAdvancedMod;

import com.arthur.JavAdvancedMod.commands.ImportBlueprintCommand;
import com.arthur.JavAdvancedMod.scrapers.ScraperBlockTranslator;
import com.arthur.JavAdvancedMod.capabilities.ModCapabilities;
import com.arthur.JavAdvancedMod.util.RegistryHandler;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(JavaAdvancedMod.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class JavaAdvancedMod
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "javaadvancedmod";
    public static final ScraperBlockTranslator scraperBlockTranslator = new ScraperBlockTranslator();

    public JavaAdvancedMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        RegistryHandler.init();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    void onServerStarting(final FMLServerStartingEvent event) {
        ImportBlueprintCommand.register(event.getServer().getCommandManager().getDispatcher());
    }

    private void setup(final FMLCommonSetupEvent event) {
        ModCapabilities.registerCapabilities();
    }
}
