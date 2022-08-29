package com.arthur.JavAdvancedMod.capabilities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBlueprintData {

    void build(World worldIn, PlayerEntity playerIn, BlockPos buildPos);

    void deserializeNBT(CompoundNBT nbt);

    void serializeNBT(CompoundNBT nbt);

    boolean hasData();

    String shortString();
}
