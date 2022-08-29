package com.arthur.JavAdvancedMod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class DrawingBoardBlock extends Block {
    public DrawingBoardBlock() {
        super(Block.Properties.create(Material.WOOD)
            .hardnessAndResistance(5.0f, 6.0f)
            .sound(SoundType.WOOD)
            .harvestLevel(0)
        );

    }
}
