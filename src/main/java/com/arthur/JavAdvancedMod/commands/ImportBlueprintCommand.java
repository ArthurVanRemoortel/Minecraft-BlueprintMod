package com.arthur.JavAdvancedMod.commands;

import com.arthur.JavAdvancedMod.JavaAdvancedMod;
import com.arthur.JavAdvancedMod.items.BlueprintItem;
import com.arthur.JavAdvancedMod.scrapers.WebScraper;
import com.arthur.JavAdvancedMod.util.BlueprintData;
import com.arthur.JavAdvancedMod.util.RegistryHandler;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.gui.overlay.DebugOverlayGui;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorld;

import java.rmi.registry.Registry;

public class ImportBlueprintCommand implements Command<CommandSource> {

    private static final ImportBlueprintCommand CMD = new ImportBlueprintCommand();

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
//        return Commands.argument("import", StringArgumentType.string())
//                .requires(cs -> cs.hasPermissionLevel(0))
//                .executes(CMD);
        dispatcher.register(example());

    }

    public static LiteralArgumentBuilder<CommandSource> example() {
        // @formatter:off
        return Commands.literal("bp")
                .then(Commands.literal("import")
                    .then(Commands.argument("url", StringArgumentType.string())
                            .executes(CMD)
                ));
        // @formatter:on
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        CommandSource source = context.getSource();
        PlayerEntity player = source.asPlayer();
        String url = StringArgumentType.getString(context, "url");
        ItemStack heldItemStack = player.inventory.getCurrentItem();
        if (heldItemStack.getItem() == RegistryHandler.BLUEPRINT.get()) {
            BlueprintItem blueprintItem = (BlueprintItem) heldItemStack.getItem();
            source.sendFeedback(ITextComponent.getTextComponentOrEmpty("importing... " + url), false);
            BlueprintData blueprintData = WebScraper.getBlueprint(url);
            if (blueprintData != null) {
                blueprintItem.assignBlueprintData(heldItemStack, blueprintData);
                source.sendFeedback(ITextComponent.getTextComponentOrEmpty("Done"), false);
            } else {
                source.sendFeedback(ITextComponent.getTextComponentOrEmpty("Failed"), false);
            }

        } else {
            source.sendFeedback(ITextComponent.getTextComponentOrEmpty("You need to hold a blueprint."), false);
        }
        return 0;
    }

}
