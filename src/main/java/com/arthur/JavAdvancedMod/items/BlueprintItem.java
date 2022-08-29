package com.arthur.JavAdvancedMod.items;

import com.arthur.JavAdvancedMod.capabilities.IBlueprintData;
import com.arthur.JavAdvancedMod.capabilities.ModCapabilities;
import com.arthur.JavAdvancedMod.util.BlueprintData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class BlueprintItem extends Item {
    public BlueprintItem() {
        super(new Properties()
                .group(ItemGroup.REDSTONE));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ModCapabilities.BlueprintProvider();
    }

    public void assignBlueprintData(ItemStack itemStack, BlueprintData data) {
        LazyOptional<IBlueprintData> cap = itemStack.getCapability(ModCapabilities.CAPABILITY_BLUEPRINT);
        cap.ifPresent( bpd -> {
            CompoundNBT nbt = new CompoundNBT();
            data.serializeNBT(nbt);
            bpd.deserializeNBT(nbt);
        });
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        ItemStack stack = context.getItem();
        PlayerEntity player = context.getPlayer();

        // Prevent this function being called twice (once on server and client)
        if (world.isRemote() || !(player instanceof ServerPlayerEntity) || stack.isEmpty()) {
            return ActionResultType.SUCCESS;
        }

        LazyOptional<IBlueprintData> cap = stack.getCapability(ModCapabilities.CAPABILITY_BLUEPRINT);
        cap.ifPresent(bpd -> {
            if (!bpd.hasData()) return;
            RayTraceResult lookingAt = Minecraft.getInstance().objectMouseOver;
            if (lookingAt != null && lookingAt.getType() == RayTraceResult.Type.BLOCK) {
                double x = lookingAt.getHitVec().getX();
                double y = lookingAt.getHitVec().getY();
                double z = lookingAt.getHitVec().getZ();
                BlockPos pos = new BlockPos(x, y, z);
                bpd.build(world, player, pos);
            }
        });
        return ActionResultType.SUCCESS;
    }
}
