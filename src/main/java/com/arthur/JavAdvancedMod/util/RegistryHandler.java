package com.arthur.JavAdvancedMod.util;

import com.arthur.JavAdvancedMod.JavaAdvancedMod;
import com.arthur.JavAdvancedMod.items.BlueprintItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, JavaAdvancedMod.MOD_ID);

    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //Items
    public static final RegistryObject<Item> BLUEPRINT = ITEMS.register("blueprint", BlueprintItem::new);
}
